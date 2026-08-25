package jp.green_code.spring_jdbc_codegen.test_app.repository;

import org.springframework.stereotype.Repository;
import jp.green_code.spring_jdbc_codegen.test_app.repository.base.BaseMapperNoPkRepository;

/**
 * Table: mapper_no_pk
 */
@Repository
public class MapperNoPkRepository extends BaseMapperNoPkRepository {
    public MapperNoPkRepository(RepositoryHelper helper) {
        super(helper);
    }
}