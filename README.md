# Npuzzle

中国海洋大学人工智能导论课大作业
## Time Line
第一部分，使用A*算法解决两个示例，在1s内完成 ——0925 已实现
### 本项目使用方法：
需要的环境：
- Git
  - 不解释，版本控制工具，会基础操作即可
- IDEA
  - 可以使用学生验证（可能会麻烦些）
  - 当然也可以用一些“其他方法”
- JDK‘corretto-19’
  - 本项目所需的JDK，clone项目之后可能有可能没有，建议直接通过IDEA内提示下载即可
### QA
- clone到本地之后无法运行：“the file in the editor is not runnable”
  - 原因：这是因为IDEA没有识别出根目录
  - 解决方法：在src目录上右击，选择Make Dictionary as ->Sources Root
    - 发现scr目录变成蓝色说明成功
- 直接运行报错（main函数在core.runner.SearchTester中）
  - 错误信息为“Exception in thread "main" java.lang.ArrayIndexOutOfBoundsException: Index 3 out of bounds for length 0”
  - 原因：本项目的运行需要向args中传入参数，直接运行项目会导致args中没有任何参数，即args[]数组长度为0，而代码中会访问args[3]这会导致数组越界
  - 解决方法：在运行的绿色三角左边点击“Current File”，在下拉菜单中找到“Edit Configurations”
    - add new->application main class框里选择SearchTester
    - program arguments里粘贴这一行“resources/problems.txt NPUZZLE 1 stud.g01.runner.PuzzleFeeder”
    - 对这一行的解释：四个参数含义依次为问题输入、问题类型、任务阶段、用于解决该问题的EngineFeeder类
- 注意
  - 运行problems输入时，里面的四行只有三阶任务（也就是任务1）能在较短时间内（1s/10s取决于使用的启发函数）解决，而最后一个示例会花费较长时间，需要改进算法
  - 大家可以输入一些其他合法输入看看程序的运行时间
  - 本项目基于老师提供的框架由我和泽伟两人完成，项目中的一些细节比较粗糙，大家可以根据自己的想法自由修改
  - 建议把problem里的问题单个单个跑，可以对比一下不同启发函数对运行速度的影响。


