# VCom4j

### _Varkhan_ Commons for Java
### High performance Java datastructures for very large datasets.


## Mission

VCom4j provides dedicated datastructures, frameworks and constructs to facilitate in-memory processing of very large datasets (Gb to Tb per node), that combine an optimized memory footprint, low latency and high throughput.


## Project structure
 
* **Base**    General purpose datastructures and constructs (no cross or external dependencies)
* **Core**    Extended functions on specific data types or environments (depends on base modules)
* **Data**   Data-storage and data-processing frameworks (depends on base modules)
* **Serv**   Serving and monitoring frameworks (depends on base and core modules and external libraries)


