This API is used to manage Employees and Departments based in it's models.

## Module Overview

| Folder                                | Content                                                  |
|---------------------------------------|----------------------------------------------------------|
| `src/main/resources`                  | API spec file in openAPI format used for code generation |
| `src/main/resources/server-templates` | API server template files.                               |
| `src/main/resources/client-templates` | API client template files.                               |

File `src/main/resources/api-specification.yaml` provides the definition
for all endpoints for Create, Read, Update and Delete operations.

## Generation

To generate client and server dependencies you can run in the [project](./) folder:

```bash
mvn clean install
```
