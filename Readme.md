Java Homework Test
- I'm using Spring Integration and Spring Batch to handle reading, transforming and writing data to CSV files.
Spring Integration will poll source folder and notify readCSVFilesJob job if there's new file coming. This job uses 
partitioner in order to read files parallel. Data will be pushed to purchase_order in H2 database.
When reading phase completed, we will launch another job orderTransformJob to transform data and write to output files.
This job contains multiple steps run parallel.
- Config source / target directories in application.properties.
- What would I improve if I have more time:
    - Add unit test and integration test.
    - Split Vendor to another database table, select distinct in a big table is not a good approach.