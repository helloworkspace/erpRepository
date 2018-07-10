package erp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.yla.ErpApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ErpApplication.class)
public class MyTest {
	
	@Test
	public void test(){
		System.out.println("hello junit");
	}

}
