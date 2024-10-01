# GenericDashbord



## Generate all services from the backend
to generate all services from the backend :
## on back end
On pom.xml
```xml
<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
				<configuration>
					<excludes>
						<exclude>
							<groupId>org.projectlombok</groupId>
							<artifactId>lombok</artifactId>
						</exclude>
					</excludes>
				</configuration>
			</plugin>

			<plugin>
				<groupId>org.springdoc</groupId>
				<artifactId>springdoc-openapi-maven-plugin</artifactId>
				<version>1.4</version>
				<executions>
					<execution>
						<id>install</id>
						<goals>
							<goal>generate</goal>
						</goals>
					</execution>
				</executions>
				<configuration>
					<apiDocsUrl>http://localhost:8080/v3/api-docs</apiDocsUrl>
					<outputFileName>openapi.json</outputFileName>
					<outputDir>${project.build.directory}</outputDir>
					<skip>false</skip>
					<headers>
						<header1key>header1value</header1key>
						<header2key>header2value</header2key>
					</headers>
				</configuration>
			</plugin>



		</plugins>
	</build>


```





```bash
mvn clean install

```

## On Frontend  project
```bash 
install ng-packgr
```
```bash 
install ng-swagger-gen
```

for new versioon of spring 3. 
we must use 
```bash 
install ng-openapi-gen
```
## Put this on Package.json

```json
{
  "...": "...",
  "serve:ssr:generic-dashbord": "node dist/generic-dashbord/server/server.mjs",
  "start:local": "npm run ds-api && ng serve -- --proxyConfig=proxy.config.json",
  "ds-api": "npm run ds-api:create-dist && npm run ds-api:download && npm run ds-api:extract && npm run ds-api:openapi-gen",
  "ds-api:create-dist": "mkdir -p tools/openapi/dist && mkdir -p tools/openapi/src",
  "ds-api:download": "cp C:/Users/Asus/Desktop/java-angular-project/delivery-system/delivery-system-api/target/deliverysystem-0.0.1-SNAPSHOT.jar ./tools/openapi/dist && cp C:/Users/Asus/Desktop/java-angular-project/delivery-system/delivery-system-api/target/openapi.json ./tools/openapi/src",
  "ds-api:extract": "node ./tools/openapi/src/openapi.json 0.0.1-SNAPSHOT",
  "ds-api:openapi-gen": "./node_modules/.bin/ng-openapi-gen -i ./tools/openapi/src/openapi.json -o src/ds-api/src"

}
```




This project was generated with [Angular CLI](https://github.com/angular/angular-cli) version 17.1.0.

## Development server

Run `ng serve` for a dev server. Navigate to `http://localhost:4200/`. The application will automatically reload if you change any of the source files.

## Code scaffolding

Run `ng generate component component-name` to generate a new component. You can also use `ng generate directive|pipe|service|class|guard|interface|enum|module`.

## Build

Run `ng build` to build the project. The build artifacts will be stored in the `dist/` directory.

## Running unit tests

Run `ng test` to execute the unit tests via [Karma](https://karma-runner.github.io).

## Running end-to-end tests

Run `ng e2e` to execute the end-to-end tests via a platform of your choice. To use this command, you need to first add a package that implements end-to-end testing capabilities.

