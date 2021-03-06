package recommend.algorithms;


import java.util.*;

import recommend.util.WordIndex;


public class CooccurSumIDF extends Algorithm {
	int[] doccount;
	HashMap<Integer,Integer>[] cooccur;
	
	public CooccurSumIDF() {
		super( "CooccurSum+IDF" );
	}
	
	public void train( List<HashMap<Integer,Double>> traindocs ) {
		doccount = new int[WordIndex.size()];
		
		for( HashMap<Integer,Double> traindoc : traindocs ) {
			for( int word : traindoc.keySet() ) {
				doccount[word]++;
			}
		}
		
		cooccur = new HashMap[WordIndex.size()];
		
		for( int i = 0; i < cooccur.length; i++ ) {
			cooccur[i] = new HashMap<Integer,Integer>();
		}
		
		for( HashMap<Integer,Double> traindoc : traindocs ) {
			for( int word1 : traindoc.keySet() ) {
				for( int word2 : traindoc.keySet() ) {
					cooccur[word1].put( word2, cooccur[word1].containsKey( word2 ) ? 1+cooccur[word1].get( word2 ) : 1 );
				}
			}
		}
	}
	
	public double[] predict( HashMap<Integer,Double> givenwords ) {
		double[] scores = new double[WordIndex.size()];
		
		for( int w1 : givenwords.keySet() ) {
			if( doccount[w1] < 4 ) {
				continue;
			}
			
			for( int w2 : cooccur[w1].keySet() ) {
				scores[w2] += (double)cooccur[w1].get( w2 ) / doccount[w1] * givenwords.get( w1 );
			}
		}
		
		for( int w = 0; w < scores.length; w++ ) {
			scores[w] *= WordIndex.getIDF( w );
		}
		
    	return scores;
	}
}
