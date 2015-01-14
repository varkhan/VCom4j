# ![VCom4j](title.png)

### _Varkhan_ Commons for Java
### High performance Java datastructures for very large datasets.


## Mission

VCom4j provides dedicated datastructures, frameworks and constructs to facilitate in-memory processing of very large datasets (Gb to Tb per node), that combine an optimized memory footprint, low latency and high throughput.


## Project structure
 
* **Base**    General purpose datastructures and constructs (no cross or external dependencies)
* **Core**    Extended functions on specific data types or environments (depends on base modules)
* **Data**   Data-storage and data-processing frameworks (depends on base modules)
* **Serv**   Serving and monitoring frameworks (depends on base and core modules and external libraries)


## Building

Invoking 

`ant -f $module/build.xml clean compile test artifact`

will build source, bytecode and documentation jars in the module `.bld` directory.
Publish to your local ivy repo by adding an extra `publish` target to the above line.


## Distribution and licence

This set of libraries is distributed under LGPL v2.1. Please refer to [`LICENSE.txt`](LICENSE.txt "LGPL v2.1") for detail.

