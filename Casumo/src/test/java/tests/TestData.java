package tests;

public interface TestData {
    String runDockerCommandWindows = "docker run -d -p 8080:8080 -it casumo/devowelizer:latest";
    String deleteDockerCommandWindows = "docker rm ";
    String stopDocker = "docker stop ";
    String baseUrl = "http://localhost:8080/";
    Object[][] expectedValues = new Object[][]  {
            {"X-Powered-By","Express"},
            {"Content-Type","text/html; charset=utf-8"},
            {"Content-Length","3"},
            {"Connection","keep-alive"}

    };
}
