package jp.green_code.spring_jdbc_codegen.test_app.repository;

import org.springframework.stereotype.Repository;
import jp.green_code.spring_jdbc_codegen.test_app.repository.base.BaseOmittablePk1Repository;

/**
 * Table: omittable_pk1
 */
@Repository
public class OmittablePk1Repository extends BaseOmittablePk1Repository {
    public OmittablePk1Repository(RepositoryHelper helper) {
        super(helper);
    }
}