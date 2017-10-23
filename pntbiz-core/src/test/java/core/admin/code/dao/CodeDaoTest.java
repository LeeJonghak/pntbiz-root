package core.admin.code.dao;

import core.common.code.dao.CodeDao;
import core.common.code.domain.Code;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import testbase.JUnitTestBase;

import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 * Created by ucjung on 2017-05-24.
 */
public class CodeDaoTest extends JUnitTestBase {
    @Autowired
    private CodeDao codeDao;

    private Code code;

    @Before
    public void before() {
        code = new Code();
        code.setgCD("AGREE");
    }

    @Test
    public void getCodeListByCD() throws Exception {
        List<Code> codes = codeDao.getCodeListByCD(code);
        assertThat(codes.size(), is(2));
    }

    @Test
    public void getCodeCheck() throws Exception {

    }

    @Test
    public void getCodeInfo() throws Exception {

    }

}