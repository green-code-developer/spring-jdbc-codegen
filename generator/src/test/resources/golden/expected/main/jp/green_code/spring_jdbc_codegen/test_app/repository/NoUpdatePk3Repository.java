package jp.green_code.spring_jdbc_codegen.test_app.repository;

import org.springframework.stereotype.Repository;
import jp.green_code.spring_jdbc_codegen.test_app.repository.base.BaseNoUpdatePk3Repository;

/**
 * Table: no_update_pk3
 */
@Repository
public class NoUpdatePk3Repository extends BaseNoUpdatePk3Repository {
    public NoUpdatePk3Repository(RepositoryHelper helper) {
        super(helper);
    }
}