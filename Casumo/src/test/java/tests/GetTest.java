package tests;

import org.example.DockerManager;
import org.example.GetMehods;
import org.testng.annotations.*;

import java.io.IOException;
import java.util.Arrays;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class GetTest implements TestData{

    DockerManager docker ;
    GetMehods getRequestPerformer;
    String dockerId ;

    @BeforeSuite
    public void setUp() throws IOException, InterruptedException {
        docker = new DockerManager();
        this.dockerId =  docker.runDocker(runDockerCommandWindows);
        System.out.println("Docker is running with id" + dockerId);
        getRequestPerformer = new GetMehods(baseUrl);
    }

    @AfterSuite(alwaysRun = true)
    public void tearDOwn() throws IOException {
        System.out.println(dockerId);
        if (dockerId!= null) {
            docker.stopDOcker(stopDocker + dockerId);
            docker.removeDOcker(deleteDockerCommandWindows + dockerId);
        }
    }

    @DataProvider(name = "bodyTestDataProvider")
    public Object[][] bodyTestDataProvider(){
      return new Object[][]  {{"input","npt"},
        {"INPUT","NPT"},
        {"aeiou",""},
        {"AEIOU",""},
        {"bcdfg","bcdfg"},
        {"BCDFG","BCDFG"},
        {"AcaSa","cS"},
        {"AeIoU",""},
        {"BcDfG","BcDfG"}};
    }
    @Test(dataProvider = "bodyTestDataProvider",groups = {"functional"})
    public void getRequestBodyAndStatusCodeTest(String input,String output){
        getRequestPerformer.performGetRequest(input);
        assertEquals(getRequestPerformer.getBody(),output,"Check Logs");
        assertEquals(getRequestPerformer.getStatusCode(),200,"Check Logs");
    }
    //some test are falling due to 2 bugs found:
    // 1.if you send uppercase input the result is not the word without vowels,is the entire word
    // 2.after 4 get requests ,500 error is returned(internal server error)

    @DataProvider(name = "bodyTestNumbersDataProvider")
    public Object[][] bodyTestNumbersDataProvider(){
        return new Object[][]  {
                {"12345"},
                {"1A2B3C"},
};
    }
    @Test(dataProvider = "bodyTestNumbersDataProvider",groups = {"functional"})
    public void getRequestBodyAndStatusCodeTestNumbers(String input){
        getRequestPerformer.performGetRequest(input);
        assertEquals(getRequestPerformer.getBody(),"Only letters allowed","Check Logs");
        assertEquals(getRequestPerformer.getStatusCode(),400,"Check logs");
    }
    //this also fails due to bug -  no output should be printed if number input is sent

    @Test(groups = {"functional"})
    public void getRequestBodyTestAndStatusCodeNoInput(){
        getRequestPerformer.performGetRequest("");
        assertEquals(getRequestPerformer.getBody(),"Send GET to /:input","Check Logs");
        assertEquals(getRequestPerformer.getStatusCode(),200,"Check Logs");
    }


    @Test(groups = {"functional"})
    public void getRequestBodyAndStatusCodeTestInputTooLong(){
        char[] charArray = new char[100];
        Arrays.fill(charArray, 'a');
        String str = new String(charArray);
        getRequestPerformer.performGetRequest(str);
        assertEquals(getRequestPerformer.getBody(),"Input too long","Check Logs");
        assertEquals(getRequestPerformer.getStatusCode(),400,"Check Logs");
    }
    //here I put a 100 char string to be too long,but this test should be modified depending on restrctions


    @Test(groups = {"functional"})
    public void getRequestheadersTest(){
        getRequestPerformer.performGetRequest("input");
        for (Object[] obj : expectedValues){
        assertEquals(getRequestPerformer.getHeaderValue(obj[0].toString()),obj[1].toString(),"Check Logs");
    }
}

    @DataProvider(name = "responseTImeDataProvider")
    public Object[][] responseTImeDataProvider(){
        return new Object[][]  {
                {"input",5000},
                {"bcd",3000},
                {"a",1000},
        };
    }

    //here i tried to use as expected the number of leters *1000 ;this value should be determined either by checking requirements either by making measurements
    @Test(dataProvider = "responseTImeDataProvider",groups = {"functional"})
    public void responseTImeTestProvider(String input,int responseTimeInMilliseconds){
        getRequestPerformer.performGetRequest(input);
        System.out.println(getRequestPerformer.getResponseTIme());
        assertTrue(getRequestPerformer.getResponseTIme() <= responseTimeInMilliseconds,"Check Logs");

    }

    @Test(groups = {"performance"})
    public void getRequestLoadTest(){
        for(int i=0;i<=60;i++)
        {
            getRequestPerformer.performGetRequest("input");
            assertTrue(getRequestPerformer.getResponseTIme() <= 5000,"Check Logs");;
        }

    }
    //this test will fail since after 4 requests internal server error is encountered

}
