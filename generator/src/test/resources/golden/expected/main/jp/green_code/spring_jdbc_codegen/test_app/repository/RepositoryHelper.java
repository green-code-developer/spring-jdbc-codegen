package jp.green_code.spring_jdbc_codegen.test_app.repository;

import org.springframework.stereotype.Component;
import javax.sql.DataSource;
import jp.green_code.spring_jdbc_codegen.test_app.repository.base.BaseRepositoryHelper;

@Component
public class RepositoryHelper extends BaseRepositoryHelper {
    public RepositoryHelper(DataSource dataSource) {
        super(dataSource);
    }
}