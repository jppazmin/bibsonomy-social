// $ANTLR 3.1.3 Mar 17, 2009 19:23:44 org/bibsonomy/model/util/tagparser/TagString3.g 2011-07-27 13:17:22


package org.bibsonomy.model.util.tagparser;



import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class TagString3Lexer extends Lexer {
    public static final int SPECIAL1=9;
    public static final int WS=8;
    public static final int SPECIAL2=10;
    public static final int RIGHTARROW=5;
    public static final int LEFTARROW=4;
    public static final int SPACE=6;
    public static final int EOF=-1;
    public static final int TAG=7;

    // delegates
    // delegators

    public TagString3Lexer() {;} 
    public TagString3Lexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public TagString3Lexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "org/bibsonomy/model/util/tagparser/TagString3.g"; }

    // $ANTLR start "LEFTARROW"
    public final void mLEFTARROW() throws RecognitionException {
        try {
            int _type = LEFTARROW;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // org/bibsonomy/model/util/tagparser/TagString3.g:9:11: ( '<-' )
            // org/bibsonomy/model/util/tagparser/TagString3.g:9:13: '<-'
            {
            match("<-"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LEFTARROW"

    // $ANTLR start "RIGHTARROW"
    public final void mRIGHTARROW() throws RecognitionException {
        try {
            int _type = RIGHTARROW;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // org/bibsonomy/model/util/tagparser/TagString3.g:10:12: ( '->' )
            // org/bibsonomy/model/util/tagparser/TagString3.g:10:14: '->'
            {
            match("->"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RIGHTARROW"

    // $ANTLR start "SPACE"
    public final void mSPACE() throws RecognitionException {
        try {
            int _type = SPACE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // org/bibsonomy/model/util/tagparser/TagString3.g:128:7: ( ( ' ' | '\\t' )+ )
            // org/bibsonomy/model/util/tagparser/TagString3.g:128:9: ( ' ' | '\\t' )+
            {
            // org/bibsonomy/model/util/tagparser/TagString3.g:128:9: ( ' ' | '\\t' )+
            int cnt1=0;
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0=='\t'||LA1_0==' ') ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // org/bibsonomy/model/util/tagparser/TagString3.g:
            	    {
            	    if ( input.LA(1)=='\t'||input.LA(1)==' ' ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    if ( cnt1 >= 1 ) break loop1;
                        EarlyExitException eee =
                            new EarlyExitException(1, input);
                        throw eee;
                }
                cnt1++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SPACE"

    // $ANTLR start "WS"
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // org/bibsonomy/model/util/tagparser/TagString3.g:131:5: ( ( '\\r' | '\\n' ) )
            // org/bibsonomy/model/util/tagparser/TagString3.g:131:7: ( '\\r' | '\\n' )
            {
            if ( input.LA(1)=='\n'||input.LA(1)=='\r' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            _channel=HIDDEN;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "WS"

    // $ANTLR start "TAG"
    public final void mTAG() throws RecognitionException {
        try {
            int _type = TAG;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // org/bibsonomy/model/util/tagparser/TagString3.g:135:2: ( (~ ( '\\n' | '\\r' | '\\t' | ' ' | '-' | '<' ) | SPECIAL1 | SPECIAL2 )+ )
            // org/bibsonomy/model/util/tagparser/TagString3.g:135:4: (~ ( '\\n' | '\\r' | '\\t' | ' ' | '-' | '<' ) | SPECIAL1 | SPECIAL2 )+
            {
            // org/bibsonomy/model/util/tagparser/TagString3.g:135:4: (~ ( '\\n' | '\\r' | '\\t' | ' ' | '-' | '<' ) | SPECIAL1 | SPECIAL2 )+
            int cnt2=0;
            loop2:
            do {
                int alt2=4;
                int LA2_0 = input.LA(1);

                if ( ((LA2_0>='\u0000' && LA2_0<='\b')||(LA2_0>='\u000B' && LA2_0<='\f')||(LA2_0>='\u000E' && LA2_0<='\u001F')||(LA2_0>='!' && LA2_0<=',')||(LA2_0>='.' && LA2_0<=';')||(LA2_0>='=' && LA2_0<='\uFFFF')) ) {
                    alt2=1;
                }
                else if ( (LA2_0=='-') && ((input.LA(2)!='>'))) {
                    alt2=2;
                }
                else if ( (LA2_0=='<') && ((input.LA(2)!='-'))) {
                    alt2=3;
                }


                switch (alt2) {
            	case 1 :
            	    // org/bibsonomy/model/util/tagparser/TagString3.g:135:6: ~ ( '\\n' | '\\r' | '\\t' | ' ' | '-' | '<' )
            	    {
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='\b')||(input.LA(1)>='\u000B' && input.LA(1)<='\f')||(input.LA(1)>='\u000E' && input.LA(1)<='\u001F')||(input.LA(1)>='!' && input.LA(1)<=',')||(input.LA(1)>='.' && input.LA(1)<=';')||(input.LA(1)>='=' && input.LA(1)<='\uFFFF') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;
            	case 2 :
            	    // org/bibsonomy/model/util/tagparser/TagString3.g:136:4: SPECIAL1
            	    {
            	    mSPECIAL1(); 

            	    }
            	    break;
            	case 3 :
            	    // org/bibsonomy/model/util/tagparser/TagString3.g:137:4: SPECIAL2
            	    {
            	    mSPECIAL2(); 

            	    }
            	    break;

            	default :
            	    if ( cnt2 >= 1 ) break loop2;
                        EarlyExitException eee =
                            new EarlyExitException(2, input);
                        throw eee;
                }
                cnt2++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "TAG"

    // $ANTLR start "SPECIAL1"
    public final void mSPECIAL1() throws RecognitionException {
        try {
            int _type = SPECIAL1;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // org/bibsonomy/model/util/tagparser/TagString3.g:151:2: ({...}? => '-' )
            // org/bibsonomy/model/util/tagparser/TagString3.g:151:4: {...}? => '-'
            {
            if ( !((input.LA(2)!='>')) ) {
                throw new FailedPredicateException(input, "SPECIAL1", "input.LA(2)!='>'");
            }
            match('-'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SPECIAL1"

    // $ANTLR start "SPECIAL2"
    public final void mSPECIAL2() throws RecognitionException {
        try {
            int _type = SPECIAL2;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // org/bibsonomy/model/util/tagparser/TagString3.g:155:2: ({...}? => '<' )
            // org/bibsonomy/model/util/tagparser/TagString3.g:155:4: {...}? => '<'
            {
            if ( !((input.LA(2)!='-')) ) {
                throw new FailedPredicateException(input, "SPECIAL2", "input.LA(2)!='-'");
            }
            match('<'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SPECIAL2"

    public void mTokens() throws RecognitionException {
        // org/bibsonomy/model/util/tagparser/TagString3.g:1:8: ( LEFTARROW | RIGHTARROW | SPACE | WS | TAG | SPECIAL1 | SPECIAL2 )
        int alt3=7;
        alt3 = dfa3.predict(input);
        switch (alt3) {
            case 1 :
                // org/bibsonomy/model/util/tagparser/TagString3.g:1:10: LEFTARROW
                {
                mLEFTARROW(); 

                }
                break;
            case 2 :
                // org/bibsonomy/model/util/tagparser/TagString3.g:1:20: RIGHTARROW
                {
                mRIGHTARROW(); 

                }
                break;
            case 3 :
                // org/bibsonomy/model/util/tagparser/TagString3.g:1:31: SPACE
                {
                mSPACE(); 

                }
                break;
            case 4 :
                // org/bibsonomy/model/util/tagparser/TagString3.g:1:37: WS
                {
                mWS(); 

                }
                break;
            case 5 :
                // org/bibsonomy/model/util/tagparser/TagString3.g:1:40: TAG
                {
                mTAG(); 

                }
                break;
            case 6 :
                // org/bibsonomy/model/util/tagparser/TagString3.g:1:44: SPECIAL1
                {
                mSPECIAL1(); 

                }
                break;
            case 7 :
                // org/bibsonomy/model/util/tagparser/TagString3.g:1:53: SPECIAL2
                {
                mSPECIAL2(); 

                }
                break;

        }

    }


    protected DFA3 dfa3 = new DFA3(this);
    static final String DFA3_eotS =
        "\1\uffff\1\7\1\12\3\uffff\1\14\2\uffff\1\17\12\uffff";
    static final String DFA3_eofS =
        "\24\uffff";
    static final String DFA3_minS =
        "\3\0\3\uffff\2\0\1\uffff\2\0\1\uffff\1\0\2\uffff\1\0\4\uffff";
    static final String DFA3_maxS =
        "\3\uffff\3\uffff\1\uffff\1\0\1\uffff\1\uffff\1\0\1\uffff\1\0\2\uffff"+
        "\1\0\4\uffff";
    static final String DFA3_acceptS =
        "\3\uffff\1\3\1\4\1\5\2\uffff\1\5\2\uffff\1\5\1\uffff\1\5\1\7\1\uffff"+
        "\1\5\1\6\1\1\1\2";
    static final String DFA3_specialS =
        "\1\1\1\2\1\7\3\uffff\1\3\1\6\1\uffff\1\0\1\10\1\uffff\1\4\2\uffff"+
        "\1\5\4\uffff}>";
    static final String[] DFA3_transitionS = {
            "\11\5\1\3\1\4\2\5\1\4\22\5\1\3\14\5\1\2\16\5\1\1\uffc3\5",
            "\11\10\2\uffff\2\10\1\uffff\22\10\1\uffff\14\10\1\6\uffd2\10",
            "\11\13\2\uffff\2\13\1\uffff\22\13\1\uffff\35\13\1\11\uffc1"+
            "\13",
            "",
            "",
            "",
            "\11\10\2\uffff\2\10\1\uffff\22\10\1\uffff\14\10\1\15\16\10"+
            "\1\15\uffc3\10",
            "\1\uffff",
            "",
            "\11\13\2\uffff\2\13\1\uffff\22\13\1\uffff\14\13\1\20\16\13"+
            "\1\20\uffc3\13",
            "\1\uffff",
            "",
            "\1\uffff",
            "",
            "",
            "\1\uffff",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA3_eot = DFA.unpackEncodedString(DFA3_eotS);
    static final short[] DFA3_eof = DFA.unpackEncodedString(DFA3_eofS);
    static final char[] DFA3_min = DFA.unpackEncodedStringToUnsignedChars(DFA3_minS);
    static final char[] DFA3_max = DFA.unpackEncodedStringToUnsignedChars(DFA3_maxS);
    static final short[] DFA3_accept = DFA.unpackEncodedString(DFA3_acceptS);
    static final short[] DFA3_special = DFA.unpackEncodedString(DFA3_specialS);
    static final short[][] DFA3_transition;

    static {
        int numStates = DFA3_transitionS.length;
        DFA3_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA3_transition[i] = DFA.unpackEncodedString(DFA3_transitionS[i]);
        }
    }

    class DFA3 extends DFA {

        public DFA3(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 3;
            this.eot = DFA3_eot;
            this.eof = DFA3_eof;
            this.min = DFA3_min;
            this.max = DFA3_max;
            this.accept = DFA3_accept;
            this.special = DFA3_special;
            this.transition = DFA3_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( LEFTARROW | RIGHTARROW | SPACE | WS | TAG | SPECIAL1 | SPECIAL2 );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA3_9 = input.LA(1);

                         
                        int index3_9 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((LA3_9>='\u0000' && LA3_9<='\b')||(LA3_9>='\u000B' && LA3_9<='\f')||(LA3_9>='\u000E' && LA3_9<='\u001F')||(LA3_9>='!' && LA3_9<=',')||(LA3_9>='.' && LA3_9<=';')||(LA3_9>='=' && LA3_9<='\uFFFF')) && ((input.LA(2)!='>'))) {s = 11;}

                        else if ( (LA3_9=='-'||LA3_9=='<') && ((input.LA(2)!='>'))) {s = 16;}

                        else s = 15;

                         
                        input.seek(index3_9);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA3_0 = input.LA(1);

                        s = -1;
                        if ( (LA3_0=='<') ) {s = 1;}

                        else if ( (LA3_0=='-') ) {s = 2;}

                        else if ( (LA3_0=='\t'||LA3_0==' ') ) {s = 3;}

                        else if ( (LA3_0=='\n'||LA3_0=='\r') ) {s = 4;}

                        else if ( ((LA3_0>='\u0000' && LA3_0<='\b')||(LA3_0>='\u000B' && LA3_0<='\f')||(LA3_0>='\u000E' && LA3_0<='\u001F')||(LA3_0>='!' && LA3_0<=',')||(LA3_0>='.' && LA3_0<=';')||(LA3_0>='=' && LA3_0<='\uFFFF')) ) {s = 5;}

                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA3_1 = input.LA(1);

                         
                        int index3_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA3_1=='-') ) {s = 6;}

                        else if ( ((LA3_1>='\u0000' && LA3_1<='\b')||(LA3_1>='\u000B' && LA3_1<='\f')||(LA3_1>='\u000E' && LA3_1<='\u001F')||(LA3_1>='!' && LA3_1<=',')||(LA3_1>='.' && LA3_1<='\uFFFF')) && ((input.LA(2)!='-'))) {s = 8;}

                        else s = 7;

                         
                        input.seek(index3_1);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA3_6 = input.LA(1);

                         
                        int index3_6 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((LA3_6>='\u0000' && LA3_6<='\b')||(LA3_6>='\u000B' && LA3_6<='\f')||(LA3_6>='\u000E' && LA3_6<='\u001F')||(LA3_6>='!' && LA3_6<=',')||(LA3_6>='.' && LA3_6<=';')||(LA3_6>='=' && LA3_6<='\uFFFF')) && ((input.LA(2)!='-'))) {s = 8;}

                        else if ( (LA3_6=='-'||LA3_6=='<') && ((input.LA(2)!='-'))) {s = 13;}

                        else s = 12;

                         
                        input.seek(index3_6);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA3_12 = input.LA(1);

                         
                        int index3_12 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (!(((input.LA(2)!='-')))) ) {s = 18;}

                        else if ( ((input.LA(2)!='-')) ) {s = 16;}

                         
                        input.seek(index3_12);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA3_15 = input.LA(1);

                         
                        int index3_15 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (!(((input.LA(2)!='>')))) ) {s = 19;}

                        else if ( ((input.LA(2)!='>')) ) {s = 16;}

                         
                        input.seek(index3_15);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA3_7 = input.LA(1);

                         
                        int index3_7 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((input.LA(2)!='-')) ) {s = 13;}

                        else if ( ((input.LA(2)!='-')) ) {s = 14;}

                         
                        input.seek(index3_7);
                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA3_2 = input.LA(1);

                         
                        int index3_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (LA3_2=='>') ) {s = 9;}

                        else if ( ((LA3_2>='\u0000' && LA3_2<='\b')||(LA3_2>='\u000B' && LA3_2<='\f')||(LA3_2>='\u000E' && LA3_2<='\u001F')||(LA3_2>='!' && LA3_2<='=')||(LA3_2>='?' && LA3_2<='\uFFFF')) && ((input.LA(2)!='>'))) {s = 11;}

                        else s = 10;

                         
                        input.seek(index3_2);
                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA3_10 = input.LA(1);

                         
                        int index3_10 = input.index();
                        input.rewind();
                        s = -1;
                        if ( ((input.LA(2)!='>')) ) {s = 16;}

                        else if ( ((input.LA(2)!='>')) ) {s = 17;}

                         
                        input.seek(index3_10);
                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 3, _s, input);
            error(nvae);
            throw nvae;
        }
    }
 

}