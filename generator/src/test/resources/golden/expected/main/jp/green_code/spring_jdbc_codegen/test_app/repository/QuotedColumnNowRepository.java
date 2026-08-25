package jp.green_code.spring_jdbc_codegen.test_app.repository;

import org.springframework.stereotype.Repository;
import jp.green_code.spring_jdbc_codegen.test_app.repository.base.BaseQuotedColumnNowRepository;

/**
 * Table: quoted_column_now
 */
@Repository
public class QuotedColumnNowRepository extends BaseQuotedColumnNowRepository {
    public QuotedColumnNowRepository(RepositoryHelper helper) {
        super(helper);
    }
}