/**
 *  
 *  BibSonomy-BibTeX-Parser - BibTeX Parser from
 * 		http://www-plan.cs.colorado.edu/henkel/stuff/javabib/
 *   
 *  Copyright (C) 2006 - 2010 Knowledge & Data Engineering Group, 
 *                            University of Kassel, Germany
 *                            http://www.kde.cs.uni-kassel.de/
 *  
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

/*
 * Created on Jul 24, 2004
 *
 * @author henkel@cs.colorado.edu
 * 
 */
package bibtex.expansions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * @author henkel
 */
class BibtexPersonListParserTests {

    public static class Test {
        private final String string;
        private final String preLast;
        private final String last;
        private final String lineage;
        private final String first;

        public Test(String string,String preLast, String last, String lineage, String first){
            this.string = string;
            this.preLast = preLast;
            this.last = last;
            this.lineage = lineage;
            this.first = first;
        }
        
        
        /**
         * @return Returns the first.
         */
        public String getFirst() {
            return first;
        }
        /**
         * @return Returns the last.
         */
        public String getLast() {
            return last;
        }
        /**
         * @return Returns the lineage.
         */
        public String getLineage() {
            return lineage;
        }
        /**
         * @return Returns the preLast.
         */
        public String getPreLast() {
            return preLast;
        }
        /**
         * @return Returns the string.
         */
        public String getString() {
            return string;
        }
    }
    
    public static Test [] tests;
    
    static {
        try {
            BufferedReader in = 
                new BufferedReader(new FileReader("/home/machine/henkel/projects/26_javabib/personparsing/RESULTS.txt"));
            String line;
            ArrayList testsAsList = new ArrayList();
            int count=0;
            while((line=in.readLine())!=null){
                System.out.print("."); count++; count%=80; if(count==0) System.out.println();
                String components [] = line.split("\\|");
                for(int i=0;i<components.length;i++){
                    components[i]=components[i].replace('~',' ');
                    if(components[i].equals("")) components[i]=null;
                    
                }
                if(components.length!=6){
                    System.err.println("\nError parsing "+line);
                    continue;
                }
                testsAsList.add(new Test(
                  components[0],
                  components[2],
                  components[3],
                  components[4],
                  components[1]
                ));
            }
            tests = new Test[testsAsList.size()];
            testsAsList.toArray(tests);
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    
}
