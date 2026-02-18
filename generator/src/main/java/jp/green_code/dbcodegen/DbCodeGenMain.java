package jp.green_code.dbcodegen;

/**
 * Entity Repository 自動生成ツール
 */
public class DbCodeGenMain {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("使い方 java -jar dbcodegen.jar c:/param.yml");
            System.out.println("  第1引数にparam.yml ファイルのパスを指定して下さい");
            return;
        }
        System.out.println("dbcodegen start");
        var runner = new DbCodeGenRunner();
        runner.run(args[0]);
        System.out.println("dbcodegen end");
    }
}