package jp.green_code.spring_jdbc_codegen.test_app.repository;

import org.springframework.stereotype.Repository;
import jp.green_code.spring_jdbc_codegen.test_app.repository.base.BaseNoUpdatePk0Repository;

/**
 * Table: no_update_pk0
 */
@Repository
public class NoUpdatePk0Repository extends BaseNoUpdatePk0Repository {
    public NoUpdatePk0Repository(RepositoryHelper helper) {
        super(helper);
    }
}