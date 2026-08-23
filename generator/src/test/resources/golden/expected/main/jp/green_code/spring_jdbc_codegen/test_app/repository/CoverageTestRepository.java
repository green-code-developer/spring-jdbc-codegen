package jp.green_code.spring_jdbc_codegen.test_app.repository;

import org.springframework.stereotype.Repository;
import jp.green_code.spring_jdbc_codegen.test_app.repository.base.BaseCoverageTestRepository;

/**
 * Table: coverage_test
 */
@Repository
public class CoverageTestRepository extends BaseCoverageTestRepository {
    public CoverageTestRepository(RepositoryHelper helper) {
        super(helper);
    }
}