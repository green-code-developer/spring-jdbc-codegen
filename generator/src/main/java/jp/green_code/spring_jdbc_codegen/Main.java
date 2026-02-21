package jp.green_code.spring_jdbc_codegen;

/**
 * Entity Repository 自動生成ツール
 */
public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("使い方 java -jar spring_jdbc_codegen.jar c:/param.yml");
            System.out.println("  第1引数にparam.yml ファイルのパスを指定して下さい");
            return;
        }
        System.out.println("start");
        var runner = new Runner();
        runner.run(args[0]);
        System.out.println("end");
    }
}