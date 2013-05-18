// $ANTLR 3.1.3 Mar 17, 2009 19:23:44 org/bibsonomy/model/util/tagparser/TagString3.g 2011-07-27 13:17:21


package org.bibsonomy.model.util.tagparser;

import org.bibsonomy.model.Tag;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class TagString3Parser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "LEFTARROW", "RIGHTARROW", "SPACE", "TAG", "WS", "SPECIAL1", "SPECIAL2"
    };
    public static final int SPECIAL1=9;
    public static final int WS=8;
    public static final int SPECIAL2=10;
    public static final int RIGHTARROW=5;
    public static final int LEFTARROW=4;
    public static final int EOF=-1;
    public static final int SPACE=6;
    public static final int TAG=7;

    // delegates
    // delegators


        public TagString3Parser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public TagString3Parser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return TagString3Parser.tokenNames; }
    public String getGrammarFileName() { return "org/bibsonomy/model/util/tagparser/TagString3.g"; }



    	private Tag lastTag = null;
    	private Set<Tag> tags;
    	private Map<String, Tag> tagList = new HashMap<String, Tag>();
            
    	/**
    	 * constructor with Tag object included
    	 * @param tokens
    	 * @param tags
    	 */
    	public TagString3Parser(CommonTokenStream tokens, Set<Tag> tags) {
    		this(tokens); 
    		this.tags = tags;
    	}

    	@Override
    	public boolean mismatchIsMissingToken(IntStream input, BitSet follow) {
    		return false;
    	}
      
    	@Override
    	public boolean mismatchIsUnwantedToken(IntStream input, int ttype) {
    		return false;
    	}




    // $ANTLR start "tagstring"
    // org/bibsonomy/model/util/tagparser/TagString3.g:67:1: tagstring : ctag ( SPACE ctag )* ;
    public final void tagstring() throws RecognitionException {
        try {
            // org/bibsonomy/model/util/tagparser/TagString3.g:68:2: ( ctag ( SPACE ctag )* )
            // org/bibsonomy/model/util/tagparser/TagString3.g:68:7: ctag ( SPACE ctag )*
            {
            pushFollow(FOLLOW_ctag_in_tagstring83);
            ctag();

            state._fsp--;

            // org/bibsonomy/model/util/tagparser/TagString3.g:68:12: ( SPACE ctag )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==SPACE) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // org/bibsonomy/model/util/tagparser/TagString3.g:68:14: SPACE ctag
            	    {
            	    match(input,SPACE,FOLLOW_SPACE_in_tagstring87); 
            	    pushFollow(FOLLOW_ctag_in_tagstring89);
            	    ctag();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);


            }

        }

            catch (final Exception e) {
            	// ignore
            }
        finally {
        }
        return ;
    }
    // $ANTLR end "tagstring"


    // $ANTLR start "ctag"
    // org/bibsonomy/model/util/tagparser/TagString3.g:72:1: ctag : t= tag ( uprel | lorel )* ;
    public final void ctag() throws RecognitionException {
        Tag t = null;



        		if (tags.size() >= Tag.MAX_TAGS_ALLOWED) return;
        	
        try {
            // org/bibsonomy/model/util/tagparser/TagString3.g:76:5: (t= tag ( uprel | lorel )* )
            // org/bibsonomy/model/util/tagparser/TagString3.g:76:9: t= tag ( uprel | lorel )*
            {
            pushFollow(FOLLOW_tag_in_ctag120);
            t=tag();

            state._fsp--;

            lastTag=t;
            // org/bibsonomy/model/util/tagparser/TagString3.g:76:30: ( uprel | lorel )*
            loop2:
            do {
                int alt2=3;
                int LA2_0 = input.LA(1);

                if ( (LA2_0==LEFTARROW) ) {
                    alt2=1;
                }
                else if ( (LA2_0==RIGHTARROW) ) {
                    alt2=2;
                }


                switch (alt2) {
            	case 1 :
            	    // org/bibsonomy/model/util/tagparser/TagString3.g:76:31: uprel
            	    {
            	    pushFollow(FOLLOW_uprel_in_ctag125);
            	    uprel();

            	    state._fsp--;


            	    }
            	    break;
            	case 2 :
            	    // org/bibsonomy/model/util/tagparser/TagString3.g:76:39: lorel
            	    {
            	    pushFollow(FOLLOW_lorel_in_ctag129);
            	    lorel();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);


            }

        }

            catch (final Exception e) {
            	// ignore
            }
        finally {
        }
        return ;
    }
    // $ANTLR end "ctag"


    // $ANTLR start "uprel"
    // org/bibsonomy/model/util/tagparser/TagString3.g:81:1: uprel : LEFTARROW t= tag ;
    public final void uprel() throws RecognitionException {
        Tag t = null;


        try {
            // org/bibsonomy/model/util/tagparser/TagString3.g:82:5: ( LEFTARROW t= tag )
            // org/bibsonomy/model/util/tagparser/TagString3.g:82:9: LEFTARROW t= tag
            {
            match(input,LEFTARROW,FOLLOW_LEFTARROW_in_uprel155); 
            pushFollow(FOLLOW_tag_in_uprel161);
            t=tag();

            state._fsp--;


                      if (lastTag == null || t == null) return;
                        lastTag.addSubTag(t);
                        t.addSuperTag(lastTag);
                        lastTag=t;
            		    

            }

        }

            catch (final Exception e) {
            	// ignore
            }
        finally {
        }
        return ;
    }
    // $ANTLR end "uprel"


    // $ANTLR start "lorel"
    // org/bibsonomy/model/util/tagparser/TagString3.g:94:1: lorel : RIGHTARROW t= tag ;
    public final void lorel() throws RecognitionException {
        Tag t = null;


        try {
            // org/bibsonomy/model/util/tagparser/TagString3.g:95:5: ( RIGHTARROW t= tag )
            // org/bibsonomy/model/util/tagparser/TagString3.g:95:9: RIGHTARROW t= tag
            {
            match(input,RIGHTARROW,FOLLOW_RIGHTARROW_in_lorel193); 
            pushFollow(FOLLOW_tag_in_lorel200);
            t=tag();

            state._fsp--;


                     if (lastTag == null || t == null) return;
                       t.addSubTag(lastTag);
                       lastTag.addSuperTag(t);
            			     lastTag=t;
            		    

            }

        }

            catch (final Exception e) {
            	// ignore
            }
        finally {
        }
        return ;
    }
    // $ANTLR end "lorel"


    // $ANTLR start "norel"
    // org/bibsonomy/model/util/tagparser/TagString3.g:104:1: norel : ( LEFTARROW | RIGHTARROW );
    public final void norel() throws RecognitionException {
        try {
            // org/bibsonomy/model/util/tagparser/TagString3.g:105:5: ( LEFTARROW | RIGHTARROW )
            // org/bibsonomy/model/util/tagparser/TagString3.g:
            {
            if ( (input.LA(1)>=LEFTARROW && input.LA(1)<=RIGHTARROW) ) {
                input.consume();
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

        }

            catch (final Exception e) {
            	// ignore
            }
        finally {
        }
        return ;
    }
    // $ANTLR end "norel"


    // $ANTLR start "tag"
    // org/bibsonomy/model/util/tagparser/TagString3.g:109:1: tag returns [Tag t = null] : tt= TAG ;
    public final Tag tag() throws RecognitionException {
        Tag t =  null;

        Token tt=null;

        try {
            // org/bibsonomy/model/util/tagparser/TagString3.g:110:5: (tt= TAG )
            // org/bibsonomy/model/util/tagparser/TagString3.g:110:9: tt= TAG
            {
            tt=(Token)match(input,TAG,FOLLOW_TAG_in_tag261); 

            						if (!tagList.containsKey(tt.getText())) {
            							t = new Tag(tt.getText());
            							                      
            							tags.add(t);
            							tagList.put(tt.getText(), t);
            						} else {
            						  t = tagList.get(tt.getText());
            						}
                    

            }

        }

            catch (final Exception e) {
            	// ignore
            }
        finally {
        }
        return t;
    }
    // $ANTLR end "tag"

    // Delegated rules


 

    public static final BitSet FOLLOW_ctag_in_tagstring83 = new BitSet(new long[]{0x0000000000000042L});
    public static final BitSet FOLLOW_SPACE_in_tagstring87 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_ctag_in_tagstring89 = new BitSet(new long[]{0x0000000000000042L});
    public static final BitSet FOLLOW_tag_in_ctag120 = new BitSet(new long[]{0x0000000000000032L});
    public static final BitSet FOLLOW_uprel_in_ctag125 = new BitSet(new long[]{0x0000000000000032L});
    public static final BitSet FOLLOW_lorel_in_ctag129 = new BitSet(new long[]{0x0000000000000032L});
    public static final BitSet FOLLOW_LEFTARROW_in_uprel155 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_tag_in_uprel161 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RIGHTARROW_in_lorel193 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_tag_in_lorel200 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_norel0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TAG_in_tag261 = new BitSet(new long[]{0x0000000000000002L});

}