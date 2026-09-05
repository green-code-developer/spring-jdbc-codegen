package jp.green_code.spring_jdbc_codegen.test_app.repository;

import org.springframework.stereotype.Repository;
import jp.green_code.spring_jdbc_codegen.test_app.repository.base.BaseIdentityPkRepository;

/**
 * Table: identity_pk
 */
@Repository
public class IdentityPkRepository extends BaseIdentityPkRepository {
    public IdentityPkRepository(RepositoryHelper helper) {
        super(helper);
    }
}