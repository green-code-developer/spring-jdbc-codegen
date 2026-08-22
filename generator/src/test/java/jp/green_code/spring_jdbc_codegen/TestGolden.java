package jp.green_code.spring_jdbc_codegen;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

import static org.apache.commons.io.FileUtils.deleteDirectory;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * golden テスト
 * <p>
 * golden/param.yml で build/golden-actual へコードを生成し、
 * golden/expected と1バイトも違わないことを確認する。
 * <p>
 * 仕様変更で出力が変わる場合は {@code make golden-update} でexpected を更新し、
 * その差分をレビュー対象とする。
 */
public class TestGolden {

    static final Path PROJECT_DIR = Path.of(System.getProperty("user.dir"));
    static final Path GOLDEN_DIR = PROJECT_DIR.resolve("src/test/resources/golden");
    static final Path PARAM_YML = GOLDEN_DIR.resolve("param.yml");
    static final Path EXPECTED_DIR = GOLDEN_DIR.resolve("expected");
    static final Path ACTUAL_DIR = PROJECT_DIR.resolve("build/golden-actual");

    @Test
    void golden() throws Exception {
        deleteDirectory(ACTUAL_DIR.toFile());
        new Runner().run(PARAM_YML.toString());

        if (!Files.isDirectory(EXPECTED_DIR)) {
            fail("golden がありません。`make golden-update` で作成してください: %s".formatted(EXPECTED_DIR));
        }
        compare();
    }

    void compare() throws IOException {
        var expectedFiles = listFiles(EXPECTED_DIR);
        var actualFiles = listFiles(ACTUAL_DIR);
        var errors = new ArrayList<String>();

        expectedFiles.stream().filter(f -> !actualFiles.contains(f))
                .forEach(f -> errors.add("生成されなかった: %s".formatted(f)));
        actualFiles.stream().filter(f -> !expectedFiles.contains(f))
                .forEach(f -> errors.add("余分に生成された: %s".formatted(f)));

        for (var f : expectedFiles) {
            if (!actualFiles.contains(f)) {
                continue;
            }
            var expected = readNormalized(EXPECTED_DIR.resolve(f));
            var actual = readNormalized(ACTUAL_DIR.resolve(f));
            if (!expected.equals(actual)) {
                errors.add("内容が異なる: %s%n%s".formatted(f, firstDiff(expected, actual)));
            }
        }
        if (!errors.isEmpty()) {
            fail("""
                    生成結果がgolden と一致しません (%d 件)
                    %s
                    意図した変更であれば `make golden-update` でexpected を更新し、差分をレビューしてください。
                    actual: %s"""
                    .formatted(errors.size(), String.join("\n", errors), ACTUAL_DIR));
        }
    }

    /** ディレクトリ配下のファイルを相対パスで列挙する */
    static Set<String> listFiles(Path dir) throws IOException {
        if (!Files.isDirectory(dir)) {
            return Set.of();
        }
        try (Stream<Path> walk = Files.walk(dir)) {
            var result = new TreeSet<String>();
            walk.filter(Files::isRegularFile)
                    .map(p -> dir.relativize(p).toString().replace('\\', '/'))
                    .forEach(result::add);
            return result;
        }
    }

    /** 改行コードの差異は比較対象としない */
    static String readNormalized(Path file) throws IOException {
        return Files.readString(file).replace("\r\n", "\n");
    }

    /** 最初に異なる行を返す */
    static String firstDiff(String expected, String actual) {
        List<String> e = expected.lines().toList();
        List<String> a = actual.lines().toList();
        for (int i = 0; i < Math.max(e.size(), a.size()); i++) {
            var el = i < e.size() ? e.get(i) : "(行なし)";
            var al = i < a.size() ? a.get(i) : "(行なし)";
            if (!el.equals(al)) {
                return "  %d 行目%n    expected: %s%n    actual  : %s".formatted(i + 1, el, al);
            }
        }
        return "  (行の差分なし。末尾の改行を確認してください)";
    }
}
