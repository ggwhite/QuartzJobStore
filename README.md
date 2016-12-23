# Quartz Job Store With Spring

簡單的測試Quartz Job Store

先寫好基本的Job讓單機可以跑

這邊寫了兩個做一樣事情的(就是記Log)Job

SimpleJob：
``` java
@Component
public class SimpleJob extends QuartzJobBean {
    private Logger logger = Logger.getLogger(getClass());
    @Override
    protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
        logger.info("Running...");
    }
}
```

TestJob：
``` java 
@Component
public class TestJob implements Job {
    private Logger logger = Logger.getLogger(getClass());
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        logger.info("Running...");
    }
}
```

> 這邊兩個Job使用不同方式做，但效果都一樣
> SimpleJob是繼承Spring的QuartzJobBean
> TestJob則直接實作Quartz的Job


Spring設定檔：
``` xml
<bean name="simpleJobDetail" class="org.springframework.scheduling.quartz.JobDetailFactoryBean">
    <property name="jobClass" value="com.ggw.quartz.job.SimpleJob" />
    <property name="durability" value="true" />
</bean>

<bean id="simpleJobTrigger" class="org.springframework.scheduling.quartz.CronTriggerFactoryBean">
    <property name="jobDetail" ref="simpleJobDetail" />
    <property name="cronExpression" value="* * * * * ? *" />
</bean>
	
<bean id="testJobDetail" class="org.springframework.scheduling.quartz.JobDetailFactoryBean">
    <property name="jobClass" value="com.ggw.quartz.job.TestJob" />
    <property name="durability" value="true" />
    <property name="description" value="Invoke Sample Job service..." />
</bean>
	
<bean id="testJobTrigger" class="org.springframework.scheduling.quartz.SimpleTriggerFactoryBean">
    <property name="jobDetail" ref="testJobDetail" />
    <property name="repeatInterval" value="5000" />
    <property name="repeatCount" value="-1" />
</bean>

<bean id="quartzScheduler" class="org.springframework.scheduling.quartz.SchedulerFactoryBean">
    <property name="autoStartup" value="true" />
    <property name="triggers">
        <list>
            <ref bean="testJobTrigger"/>
            <ref bean="simpleJobTrigger"/>
        </list>
    </property>
    <property name="jobDetails">
        <list>
            <ref bean="testJobDetail" />
            <ref bean="simpleJobDetail" />
        </list>
    </property>
</bean>
```
> 設定兩個Tirgger，分別使用**CronTrigger**和**SimpleTrigger**
> 這邊SimpleJob使用CronTrigger，並每秒都執行一次
> TestJob使用SimpleTrigger，每5秒執行一次，重複次數```-1```表示無限次數

執行的結果像這樣

``` log
16:47:50,547  INFO SimpleJob [quartzScheduler_Worker-1]:16 - Running...
16:47:51,051  INFO SimpleJob [quartzScheduler_Worker-2]:16 - Running...
16:47:51,685  INFO TestJob [quartzScheduler_Worker-3]:15 - Running...
16:47:52,063  INFO SimpleJob [quartzScheduler_Worker-4]:16 - Running...
16:47:53,043  INFO SimpleJob [quartzScheduler_Worker-5]:16 - Running...
16:47:54,044  INFO SimpleJob [quartzScheduler_Worker-6]:16 - Running...
16:47:55,039  INFO SimpleJob [quartzScheduler_Worker-7]:16 - Running...
16:47:56,045  INFO SimpleJob [quartzScheduler_Worker-8]:16 - Running...
16:47:56,667  INFO TestJob [quartzScheduler_Worker-9]:15 - Running...
16:47:57,047  INFO SimpleJob [quartzScheduler_Worker-10]:16 - Running...
```

這樣一般正常的QuartzJob就完成了

## Quartz Job Store

就是有N個Instance(主機 or App)可以執行Job，而該由哪些主機去執行由Database控管，[官方說明](http://www.quartz-scheduler.org/documentation/quartz-2.x/configuration/ConfigJDBCJobStoreClustering.html)，好處是可以做到Load Balance和Fail Over。

因為剛剛做好了一般的QuartzJob，現在只是想要讓他的執行由DB控管，所以只要改一下設定就可以了
這邊把Quartz的設定檔抽出來```src/main/resource/quartz.properties```
在Spring設定檔的地方指定
``` xml
<bean id="quartzScheduler" class="org.springframework.scheduling.quartz.SchedulerFactoryBean">
    <property name="configLocation" value="classpath:quartz.properties" />
    <property name="triggers">
        <list>
            <ref bean="testJobTrigger"/>
            <ref bean="simpleJobTrigger"/>
        </list>
    </property>
    <property name="jobDetails">
        <list>
            <ref bean="testJobDetail" />
            <ref bean="simpleJobDetail" />
        </list>
    </property>
</bean>
```
> JobDetail和Trigger還是要宣告，autoStartup就不要設定true了，因為是交由DB控管了。

quartz.properties：
``` properties
#============================================================================
# Configure Main Scheduler Properties  
#============================================================================
org.quartz.scheduler.instanceName = MyClusteredScheduler
org.quartz.scheduler.instanceId = AUTO
#============================================================================
# Configure ThreadPool  
#============================================================================
org.quartz.threadPool.class = org.quartz.simpl.SimpleThreadPool
org.quartz.threadPool.threadCount = 30
org.quartz.threadPool.threadPriority = 5
#============================================================================
# Configure JobStore  
#============================================================================
org.quartz.jobStore.misfireThreshold = 60000
org.quartz.jobStore.class = org.quartz.impl.jdbcjobstore.JobStoreTX
org.quartz.jobStore.driverDelegateClass = org.quartz.impl.jdbcjobstore.StdJDBCDelegate
org.quartz.jobStore.useProperties = false
org.quartz.jobStore.dataSource = myDS
org.quartz.jobStore.tablePrefix = QRTZ_
org.quartz.jobStore.isClustered = true  
org.quartz.jobStore.clusterCheckinInterval = 1000
#============================================================================
# Configure Datasources  
#============================================================================
org.quartz.dataSource.myDS.driver = com.mysql.jdbc.Driver
org.quartz.dataSource.myDS.URL = jdbc:mysql://localhost:3306/quartz_job_store
org.quartz.dataSource.myDS.user = root
org.quartz.dataSource.myDS.password = root
org.quartz.dataSource.myDS.maxConnections = 5
org.quartz.dataSource.myDS.validationQuery=select 0 from dual
org.quartz.dataSource.myDS.verifyServerCertificate=false
```

在這邊DB的Schema要自己建立，我從網路上找到的別人寫好的([這裡](https://github.com/quartznet/quartznet/tree/master/database/tables))，然後我把它加到這裡面來 ``` /schema ```，他很不錯的把各個DB的Schema寫好SQL了。

這樣基本上就可以正常執行了。

## Test

我打包了3個jar檔，寫Log分別寫到不同的檔案，來觀察他們之間的差異

在pom.xml：
``` xml
<build>
    <finalName>quartz-job-store</finalName>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-shade-plugin</artifactId>
            <version>1.7</version>
            <executions>
                <execution>
                    <phase>package</phase>
                    <goals>
                        <goal>shade</goal>
                    </goals>
                    <configuration>
                        <transformers>
                            <transformer implementation="org.apache.maven.plugins.shade.resource.AppendingTransformer">
                                <resource>META-INF/spring.handlers</resource>
                            </transformer>
                            <transformer implementation="org.apache.maven.plugins.shade.resource.AppendingTransformer">
                                <resource>META-INF/spring.schemas</resource>
                            </transformer>
                            <transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                                <mainClass>com.ggw.quartz.App</mainClass>
                            </transformer>
                        </transformers>
                    </configuration>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

log4j.properties：
``` properties
log4j.path=/tmp/quartz-job-store
```

> 為了方便包成了3個jar檔，分別修該pom.xml的 ```<finalName>```為
> * ``` <finalName>quartz-job-store-1</finalName> ```
> * ``` <finalName>quartz-job-store-2</finalName> ```
> * ``` <finalName>quartz-job-store-3</finalName> ```
> 
> log4j.properites路徑改為
> * ``` log4j.path=/tmp/quartz-job-store-1 ```
> * ``` log4j.path=/tmp/quartz-job-store-2 ```
> * ``` log4j.path=/tmp/quartz-job-store-3 ```
> 
>　分三次打包，``` mvn package ```

執行jar檔，當作是一個**Instance**

## 觀察

#### Scenario - 三個**Instance**同時執行中
* **quartz-job-store-1**：一直記錄SimpleJob的Log
* **quartz-job-store-2**：一直記錄TestJob的Log
* **quartz-job-store-3**：沒有在執行Job

#### Scenario - **quartz-job-store-1**終止
* **quartz-job-store-1**：被關閉(或當機...)
* **quartz-job-store-2**：一直記錄TestJob的Log
* **quartz-job-store-3**：開始記錄SimpleJob的Log

#### Scenario - **quartz-job-store-2**終止
* **quartz-job-store-1**：被關閉(或當機...)
* **quartz-job-store-2**：被關閉(或當機...)
* **quartz-job-store-3**：SimpleJob和TestJob都由他來執行處理

> 在中斷Instance後，其他有在Run Job的Instance不會馬上的幫被中斷的Instance接續工作，但是過了一下子會一次執行好幾次，猜測是他還不知道對方掛掉，應該是```quartz.properties```設定中的```org.quartz.jobStore.clusterCheckinInterval = 20000```，20秒才去DB問，所以在這中間空檔被中斷的Instance少做了幾次，DB會跟他說你要再幫它把沒做到的都做完，這應該可以調整說沒有執行到的就算了。

#### Scenario - **quartz-job-store-1**復活
* **quartz-job-store-1**：把一個Job分來做，可能是SimpleJob或是TestJob
* **quartz-job-store-2**：被關閉(或當機...)
* **quartz-job-store-3**：會有Job被分走

> 3個Instance總共有2個Job要執行，感覺像是Load Balance把Job分配出去，因為現在Job太少看不出來，如果有100個Job會不會每人分個30幾個Job在執行...？可以試試...
