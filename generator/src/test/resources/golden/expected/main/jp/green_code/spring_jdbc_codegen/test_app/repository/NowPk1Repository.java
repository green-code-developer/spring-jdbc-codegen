package jp.green_code.spring_jdbc_codegen.test_app.repository;

import org.springframework.stereotype.Repository;
import jp.green_code.spring_jdbc_codegen.test_app.repository.base.BaseNowPk1Repository;

/**
 * Table: now_pk1
 */
@Repository
public class NowPk1Repository extends BaseNowPk1Repository {
    public NowPk1Repository(RepositoryHelper helper) {
        super(helper);
    }
}