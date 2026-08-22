package jp.green_code.spring_jdbc_codegen.test_app.repository;

import org.springframework.stereotype.Repository;
import jp.green_code.spring_jdbc_codegen.test_app.repository.base.BaseOmittablePk0Repository;

/**
 * Table: omittable_pk0
 */
@Repository
public class OmittablePk0Repository extends BaseOmittablePk0Repository {
    public OmittablePk0Repository(RepositoryHelper helper) {
        super(helper);
    }
}