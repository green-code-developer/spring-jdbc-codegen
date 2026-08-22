package jp.green_code.spring_jdbc_codegen.test_app.repository;

import org.springframework.stereotype.Repository;
import jp.green_code.spring_jdbc_codegen.test_app.repository.base.Base日本語tableRepository;

/**
 * Table: 日本語Table
 */
@Repository
public class 日本語tableRepository extends Base日本語tableRepository {
    public 日本語tableRepository(RepositoryHelper helper) {
        super(helper);
    }
}