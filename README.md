# Rest-api testing scripts 

Very simple test framework on java for testing rest api.
  
### Based on
For testing:
- junit5
- rest-assured
- json assert

For code quality / readiness:
- lombok
- sonar

Other:
- jackson 

### Reports:
- log file
- ELK
- allure

### Run
```bash  
mvn clean test
```
or
```bash  
mvn clean test -Drest.server.url="http://habr.com" -Drest.username="admin" -Drest.password="admin"
```

### Generate report
```bash  
mvn allure:serve
```

### Troubleshooting
- For checking connection to ELK set `configuration debug="true"` in logback.xml and see log print out

