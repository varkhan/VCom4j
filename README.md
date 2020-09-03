# ![VCom4j](title.png)

### _Varkhan_ Commons for Java
#### High performance Java constructs and datastructures for very large datasets.


## Mission

VCom4j provides dedicated datastructures, frameworks and constructs to facilitate in-memory processing of very large datasets (Gbs to Tbs per node), that combine an optimized memory footprint, low latency and high throughput.


## Packages
 
* [**Base**](Base)   General purpose datastructures and constructs (no cross or external dependencies)
   * [**Containers**](Base/Containers)      Container-type datastructures (lists, sets, maps, trees, tries, queues)
   * [**Conversion**](Base/Conversion)      Format conversion, (en/de)coding and (de)serialization
   * [**Extensions**](Base/Extensions)      Language extensions, typing and syntactic tools
   * [**Functors**](Base/Functors)          Functional primitives and frameworks
   * [**Management**](Base/Management)      Configuration, logging and monitoring constructs

* [**Core**](Core)   Extended functions on specific data types or environments (depends only on **Base** modules)
   * [**Concurrent**](Core/Concurrent)      Thread-safe datastructures
   * [**Geometry**](Core/Geometry)          Geometry data representation, search and storage
   * [**Presentation**](Core/Presentation)  Visual rendering primitives and frameworks

* [**Data**](Data)   Data-storage and data-processing frameworks (depends on **Base** and **Core** modules)
   * [**Learning**](Data/Learning)          Machine-learning primitives and algorithms
   * [**Linguistics**](Data/Linguistics)    NLP and character data analysis structures
   * [**VisualDiff**](Data/VisualDiff)      Change detection and comparison algorithms

* **Serv**   Serving and monitoring frameworks (depends on **Base** and **Core** modules and external libraries)


## Building

For each _$module_, invoking

`ant -f $module/build.xml clean compile test artifact`

will build source, bytecode and documentation jars in the local `$module/lib` directory.

Adding an extra `publish` target to the above line will publish to the local repo, by default `.ivy/dist` in the project root.


## Distribution and licence

This set of libraries is distributed under LGPL v2.1. Please refer to [`LICENSE.txt`](LICENSE.txt "LGPL v2.1") for detail.

