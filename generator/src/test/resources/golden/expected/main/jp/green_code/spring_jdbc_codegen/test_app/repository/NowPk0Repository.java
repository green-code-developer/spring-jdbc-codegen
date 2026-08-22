package jp.green_code.spring_jdbc_codegen.test_app.repository;

import org.springframework.stereotype.Repository;
import jp.green_code.spring_jdbc_codegen.test_app.repository.base.BaseNowPk0Repository;

/**
 * Table: now_pk0
 */
@Repository
public class NowPk0Repository extends BaseNowPk0Repository {
    public NowPk0Repository(RepositoryHelper helper) {
        super(helper);
    }
}