/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.ml.tracking.clas12;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.deeplearning4j.optimize.listeners.TimeIterationListener;
import org.deeplearning4j.util.ModelSerializer;
import org.jlab.ml.tracking.nn.LineImageDataIterator;
import org.jlab.ml.tracking.nn.RunTraining;
import org.jlab.ml.tracking.nn.TrackingModel;
import org.nd4j.linalg.api.ndarray.INDArray;

/**
 *
 * @author gavalian
 */
public class RunClas12Training {
    
     public static void main(String[] args){
        
        System.out.println("running ML tracking learning algorithm...");
        
        
        int nIterations = Integer.parseInt(args[0]);
        
        System.out.println(" SET ITERATIONS = " + nIterations);
        
        Clas12TrackingModel model = new Clas12TrackingModel();
        
        MultiLayerConfiguration configuration = model.getConfiguration();
                
        MultiLayerNetwork network = new MultiLayerNetwork(configuration);        
        network.init();
        
        network.addListeners(new ScoreIterationListener(1));        
        network.addListeners(new TimeIterationListener(1));

        
        
        
        /*LineImageDataIterator iter = new LineImageDataIterator();

        iter.readDirectory("mldata");
        INDArray  aInputs = iter.getInputArray();
        INDArray aOutputs = iter.getOutputArray();
        */
        
        String filename = "/Users/gavalian/Work/Software/project-6a.0.0/clas_004013_ML.hipo";
        Clas12DataLoader loader = new Clas12DataLoader();
        loader.readFile(filename);
        
        
        INDArray  aInputs = loader.getInputArray(  2000 );
        INDArray aOutputs = loader.getOutputArray( 2000 );
        
        List<String> names = network.getLayerNames();
        
        for(int i = 0; i < names.size(); i++){
            System.out.println(String.format("%3d : %s", i, names.get(i)));
        }
        
        for(int i = 0; i < nIterations; i++){
            System.out.println("running epoch # " + i);
            long start_time = System.currentTimeMillis();
            network.fit(aInputs, aOutputs);
            long end_time   = System.currentTimeMillis();
            long elapsed_time = end_time - start_time;
            double score = network.score();
            System.out.println("epoch # " + i + " is complete with score = " + score + " time = " + elapsed_time + " ms");
        }
        
        
        
        
        
        try {
            
            ModelSerializer.writeModel(network, "data_file.nnet", true);
                        
            /*
            TrackingModel model = new TrackingModel();
            MultiLayerConfiguration   config = model.getConfiguration();
            MultiLayerNetwork        network = new MultiLayerNetwork(config);
            
            List<String> layerNames = network.getLayerNames();
            for(int i = 0; i < layerNames.size(); i++){
            System.out.println(String.format("%3d : %12s", i, layerNames.get(i)));
            }*/
            //ZooModel zooModel = ;
        } catch (IOException ex) {
            Logger.getLogger(RunTraining.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
