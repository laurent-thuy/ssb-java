package ssb;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ Test_Cell.class, Test_Dataset.class, Test_SSBDataset.class, Test_SSBDataset_Constructor.class,
		Test_Table.class })
public class AllTests {

}
