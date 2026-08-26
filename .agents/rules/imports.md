# Import Rule

Cuando escribas o modifiques código en este proyecto, **siempre** debes realizar la importación correspondiente (`import`) en lugar de utilizar el nombre completamente cualificado (fully qualified name) en el código.

Por ejemplo:
- **Incorrecto**: `java.util.List<String> list = new java.util.ArrayList<>();`
- **Correcto**: 
  ```java
  import java.util.List;
  import java.util.ArrayList;

  // ...
  List<String> list = new ArrayList<>();
  ```
