# Required Libraries

## For Manual Setup (without Maven)

If you're not using Maven, download and add these JAR files to your project's build path:

### Core Dependencies

1. **MySQL Connector/J 8.0.33**
   - Download: https://dev.mysql.com/downloads/connector/j/
   - File: `mysql-connector-java-8.0.33.jar`

### For Web Application

2. **Servlet API 4.0.1**
   - Included with Tomcat (no separate download needed)
   - Or download from: https://mvnrepository.com/artifact/javax.servlet/javax.servlet-api

3. **JSP API 2.3.3**
   - Included with Tomcat
   - Or download from: https://mvnrepository.com/artifact/javax.servlet.jsp/javax.servlet.jsp-api

4. **JSTL 1.2**
   - Download: https://mvnrepository.com/artifact/javax.servlet/jstl/1.2
   - File: `jstl-1.2.jar`

### For Desktop Application (JavaFX)

5. **JavaFX SDK 17+**
   - Download: https://gluonhq.com/products/javafx/
   - Files needed from lib folder:
     - javafx.base.jar
     - javafx.controls.jar
     - javafx.fxml.jar
     - javafx.graphics.jar
     - javafx.media.jar (optional)
     - javafx.swing.jar (optional)
     - javafx.web.jar (optional)

### Optional Libraries

6. **JUnit 4.13.2** (for testing)
   - Download: https://mvnrepository.com/artifact/junit/junit/4.13.2

---

## Maven Users

If using Maven, all dependencies are automatically managed via `pom.xml`. Just run:
```bash
mvn clean install
```

---

## How to Add JARs in Eclipse

1. Right-click project → Build Path → Configure Build Path
2. Libraries tab → Add External JARs
3. Select the JAR files
4. Click Apply and Close

---

## How to Add JARs in IntelliJ IDEA

1. File → Project Structure → Libraries
2. Click + → Java
3. Select the JAR files
4. Click OK

---

## Folder Structure

Place downloaded JARs in this `lib` folder:
```
lib/
├── mysql-connector-java-8.0.33.jar
├── jstl-1.2.jar
└── javafx-sdk-17/
    └── lib/
        ├── javafx.base.jar
        ├── javafx.controls.jar
        └── ...
```

---

## Verification

After adding libraries, verify they're in the classpath:
- Eclipse: Check Referenced Libraries in Project Explorer
- IntelliJ: Check External Libraries in Project view
- Maven: Check Maven Dependencies folder

---

## Troubleshooting

**ClassNotFoundException for MySQL driver:**
- Ensure mysql-connector-java JAR is in classpath
- For web apps, also place in Tomcat's lib folder

**JavaFX runtime components missing:**
- Add VM arguments: `--module-path PATH_TO_FX --add-modules javafx.controls,javafx.fxml`

**Servlet classes not found:**
- Ensure Tomcat is configured as runtime server
- servlet-api.jar should be provided by Tomcat (scope: provided in pom.xml)
