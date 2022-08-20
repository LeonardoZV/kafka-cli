import br.com.leonardozv.kafka.cli.KafkaCommandLineInterfaceApplication;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class KafkaCommandLineInterfaceApplicationUnitTest {

    @Test
    public void contextLoads() {
        KafkaCommandLineInterfaceApplication.main(new String[]{});
    }

}
