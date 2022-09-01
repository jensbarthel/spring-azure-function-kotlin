# Kotlin Spring Azure Function
A minimal azure function app written in Kotlin and Spring Cloud Function.

## Usage
Copy   
`aggregateservice/local.settings-template.json` to `local.settings.json` and modify accordingly.

Run
```shell
$ ./gradlew aggregateservice:azureFunctionsRun
```

Verify
```shell
$ curl http://localhost:7071/api/someAggregate/15
```