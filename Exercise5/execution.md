# To run SonarQube analysis

1. Place you in APP folder
2. In first, to generate all reports, run :

    ```bash
    ./mvnw -Pprod verify -DskipSonar
    ```

3. In second, to run SonarQube :

    ```bash
    ./mvnw sonar:sonar   -Dsonar.host.url=http://localhost:9000   -Dsonar.token=sqp_32075d5e94cf8a33e5f6bde9e74a9ace9bc80bc5 -Dsonar.projectKey=assista-crise   -Dsonar.projectName="Assista Crise"
    ```

    > Note : In my case,
    > - Sonar is running on port 9000
    > - My token is "sqp_32075d5e94cf8a33e5f6bde9e74a9ace9bc80bc5"
    > - My projectKey is "assista-crise"
    > - My Sonar project name is "Assista Crise"
    >
    > /!\ Your case can be different /!\
