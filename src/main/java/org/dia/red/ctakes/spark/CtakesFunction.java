/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dia.red.ctakes.spark;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import org.apache.uima.UIMAException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.cas.impl.XmiCasSerializer;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.util.XMLSerializer;
import org.apache.spark.api.java.function.Function;

import it.cnr.iac.CTAKESClinicalPipelineFactory;
/**
 * @author Selina Chu, Michael Starch, and Giuseppe Totaro
 *
 */
public class CtakesFunction implements Function<String, String> {

	transient JCas jcas = null;
	transient AnalysisEngineDescription aed = null;

	private void setup() throws UIMAException {
		System.setProperty("ctakes.umlsuser", "");
		System.setProperty("ctakes.umlspw", "");
		this.jcas = JCasFactory.createJCas();
		this.aed = CTAKESClinicalPipelineFactory.getDefaultPipeline();

	}
	
	private void readObject(ObjectInputStream in) {
		try {
			in.defaultReadObject();
			this.setup();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (UIMAException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String call(String paragraph) throws Exception {

		this.jcas.setDocumentText(paragraph);
		
		// final AnalysisEngineDescription aed = getFastPipeline(); // Outputs
		// from default and fast pipelines are identical
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		SimplePipeline.runPipeline(this.jcas, this.aed);
		XmiCasSerializer xmiSerializer = new XmiCasSerializer(jcas.getTypeSystem());
		XMLSerializer xmlSerializer = new XMLSerializer(baos, true);
		xmiSerializer.serialize(jcas.getCas(),xmlSerializer.getContentHandler());
		this.jcas.reset();
		return baos.toString("utf-8");
	}

}
