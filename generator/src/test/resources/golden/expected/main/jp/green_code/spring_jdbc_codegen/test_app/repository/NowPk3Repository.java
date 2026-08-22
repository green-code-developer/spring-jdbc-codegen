package jp.green_code.spring_jdbc_codegen.test_app.repository;

import org.springframework.stereotype.Repository;
import jp.green_code.spring_jdbc_codegen.test_app.repository.base.BaseNowPk3Repository;

/**
 * Table: now_pk3
 */
@Repository
public class NowPk3Repository extends BaseNowPk3Repository {
    public NowPk3Repository(RepositoryHelper helper) {
        super(helper);
    }
}