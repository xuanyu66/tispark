/*
 * Copyright 2017 PingCAP, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.spark.sql

import com.pingcap.tispark.{TiDBRelation, TiOptions}
import org.apache.spark.internal.Logging


class TiContext (val session: SparkSession, addressList: List[String]) extends Serializable with Logging {
  val sqlContext = session.sqlContext
  def tidbTable(dbName: String,
                tableName: String): DataFrame = {
    logDebug("Creating tiContext...")
    val tiRelation = TiDBRelation(new TiOptions(addressList, dbName, tableName))(sqlContext)
    session.experimental.extraStrategies ++= Seq(new TiStrategy(sqlContext))
    sqlContext.baseRelationToDataFrame(tiRelation)
  }
}
