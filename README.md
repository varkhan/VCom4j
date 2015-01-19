# ![VCom4j](title.png)

### _Varkhan_ Commons for Java
### High performance Java datastructures for very large datasets.


## Mission

VCom4j provides dedicated datastructures, frameworks and constructs to facilitate in-memory processing of very large datasets (Gb to Tb per node), that combine an optimized memory footprint, low latency and high throughput.


## Project structure
 
* **Base**   General purpose datastructures and constructs (no cross or external dependencies)
 * **Containers**   Container-type datastructures (lists, sets, maps, trees, tries, queues)
 * **Conversion**   Format conversion, (en/de)coding and (de)serialization
 * **Functors**     Functional primitives and frameworks
 * **Management**   Configuration, logging and monitoring constructs

* **Core**   Extended functions on specific data types or environments (depends on base modules)
 * **Concurrent**   Thread-safe datastructures
 * **Geometry**     Geometry data representation, search and storage
 * **Presentation** Visual rendering primitives and frameworks

* **Data**   Data-storage and data-processing frameworks (depends on base modules)
 * **Learning**     Machine-learning primitives and algorithms
 * **Linguistics**  NLP and character data analysis structures
 * **VisualDiff**   Change detection and comparison algorithms

* **Serv**   Serving and monitoring frameworks (depends on base and core modules and external libraries)


## Building

For each _$module_, invoking

`ant -f $module/build.xml clean compile test artifact`

will build source, bytecode and documentation jars in the local `$module/lib` directory.

Adding an extra `publish` target to the above line will publish to the local repo, by default `.ivy/dist` in the project root.


## Distribution and licence

This set of libraries is distributed under LGPL v2.1. Please refer to [`LICENSE.txt`](LICENSE.txt "LGPL v2.1") for detail.

