package jp.green_code.spring_jdbc_codegen.test_app.repository;

import org.springframework.stereotype.Repository;
import jp.green_code.spring_jdbc_codegen.test_app.repository.base.BaseNoUpdatePk1Repository;

/**
 * Table: no_update_pk1
 */
@Repository
public class NoUpdatePk1Repository extends BaseNoUpdatePk1Repository {
    public NoUpdatePk1Repository(RepositoryHelper helper) {
        super(helper);
    }
}