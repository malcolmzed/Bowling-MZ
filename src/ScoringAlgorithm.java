public
class ScoringAlgorithm {

    /*
    Only limited testing has been implemented, and not properly parametised.

    The algorithm seems to work perfectly. The primary design feature is the
    FrameInfo class, which holds all of the key information about a frame.

    In particular, for a strike or spare, it holds the scores of the bonus
    points from the next frame(s) [or in the case of the final frame, the
    points from the additional ball(s)]
    */


        private static String theGame;

        public static
        class FrameInfo {
            String frameString;
            boolean strike;
            boolean spare;
            int ball1;
            int ball2;
            int baseScore;
            int frameScore;
            int bonus1;
            int bonus2;


            FrameInfo(String theFrame,
                      boolean isStrike,
                      boolean isSpare,
                      int ba1,
                      int ba2,
                      int base,
                      int frame,
                      int bo1,
                      int bo2) {
                theFrame = frameString;
                isStrike = strike;
                isSpare = spare;
                ba1 = ball1;
                ba2 = ball2;
                base = baseScore;
                frame = frameScore;
                bo1 = bonus1;
                bo2 = bonus2;
            }
        }

        public static
        FrameInfo initialiseFrame() {

            FrameInfo aFrame = new FrameInfo("",
                                             false,
                                             false,
                                             -1,
                                             -1,
                                             0,
                                             0,
                                             0,
                                             0
            );
            return aFrame;
        }

        public static
        FrameInfo[] fillTheFrames(final String theGame) {
            System.out.println(theGame + "  ( - has been replaced with 0)");

            FrameInfo theFrames[] = new FrameInfo[10];

            String frameStrings[] = theGame.split(" ");

            for (int i = 0; i < frameStrings.length; i++) {
                theFrames[i] = initialiseFrame();

                theFrames[i].frameString = frameStrings[i];

                switch (frameStrings[i].charAt(0)) {
                    case 'X':
                        theFrames[i].strike = true;
                        theFrames[i].ball1 = 10;
                        theFrames[i].ball2 = -1;
                        theFrames[i].baseScore = 10;
                        break;

                    default:
                        theFrames[i].ball1 = frameStrings[i].charAt(0) - '0';
                        theFrames[i].baseScore = theFrames[i].ball1;


                        switch (frameStrings[i].charAt(1)) {
                            case '/':
                                theFrames[i].spare = true;
                                theFrames[i].ball2 = 10 - theFrames[i].ball1;
                                theFrames[i].baseScore = theFrames[i].ball1;
                                break;

                            default:
                                theFrames[i].ball2 = frameStrings[i].charAt(1) - '0';
                                theFrames[i].baseScore = theFrames[i].ball1 +
                                                         theFrames[i].ball2;
                                break;
                        }

                        if (i == 9) {
                            if (frameStrings[i].charAt(0) == 'X') { // last frame is a strike - need to
                                // next two chars [which might be X]
                                if (frameStrings[i].charAt(1) == 'X')
                                    theFrames[i].bonus1 = 10;
                                else
                                    theFrames[i].bonus1 = frameStrings[i].charAt(1) - '0';

                                if (frameStrings[i].charAt(2) == 'X')
                                    theFrames[i].bonus2 = 10;
                                else
                                    theFrames[i].bonus2 = frameStrings[i].charAt(2) - '0';
                            } else {
                                if (frameStrings[i].charAt(1) == '/') {// last frame is a spare - need to
                                    // next char [which might be X]
                                    if (frameStrings[i].charAt(2) == 'X')
                                        theFrames[i].bonus1 = 10;
                                    else
                                        theFrames[i].bonus1 = frameStrings[i].charAt(2) - '0';
                                }
                            }
                        }
                }
            }

            return (FrameInfo[]) theFrames;
        }

        public static
        int getNextBall(FrameInfo[] theFrames, int position) {
            char nextBallChar;
            char nextBallChar2;
            int nextBall;

            nextBallChar = theFrames[position].frameString.charAt(0);

            if (position < theFrames.length - 1) {
                nextBall = theFrames[position + 1].ball1;
            }
            else {   // last frame - need to handle cases such as XXX X52 6/7 6/X 67

                if (theFrames[position].frameString.length() < 3) return 0; // no strike or spare

                if( nextBallChar == 'X' ) {
                    nextBallChar2 = theFrames[position].frameString.charAt(1);
                    return (nextBallChar2 == 'X') ? 10 : nextBallChar2 - '0';
                }

                // must be a spare such as 6/7 or 6/X
                nextBallChar2 = theFrames[position].frameString.charAt(2);
                return (nextBallChar2 == 'X') ? 10 : nextBallChar2 - '0';
            }

            return nextBall;
        }

        public static
        int getNextBall2(FrameInfo[] theFrames, int position) { //only used for a strike

            char nextBall2Char;
            int nextBall2=0;

            if (position < 8) {
                if (theFrames[position + 1].strike)
                    nextBall2 = getNextBall(theFrames, position + 1);

                else if (theFrames[position + 1].spare)
                    nextBall2 = getNextBall(theFrames, position + 1);

                else
                    nextBall2 = theFrames[position + 1].ball2;
            }
            else if (position == 8) { // penultimate frame
                nextBall2Char = theFrames[9].frameString.charAt(1);

                if (theFrames[position].strike) {   // get the second ball from the final frame
                    nextBall2 = (nextBall2Char == 'X') ? 10 : nextBall2Char - '0';
                }

                else if (theFrames[position].spare) {  // get the second ball from the final frame
                    nextBall2 = nextBall2Char - '0';
                }
            }
            else if (position == 9) { // final frame
                nextBall2Char = theFrames[9].frameString.charAt(2);

                nextBall2 = (nextBall2Char == 'X') ? 10 : nextBall2Char - '0';
            }
            return nextBall2;
        }

        public static
        void main (String[]args){
            // Here are the games for testing, but expected results have not yet been engineered
            String games[] = {
                    "X 9- 9- 9- 9- 9- 9- 9- 9- 9-",
                    "00 00 00 00 00 00 00 00 00 00",
                    "X 52 X X 6/ 30 X X X XXX",
                    "X X X X X X X X X XXX",
                    "X -4 5- -6 4/ 23 4/ X 21 4/3",
                    "X -4 5- -6 4/ 23 4/ X X 4/3"   // this test failed - for the penultimate frame
                                                    // it counted the bonus balls [from 4/3] as 4+0
                                                    // instead of 4+6
            };

            for (int index = 0; index < games.length; index++) {
                String game = games[index];

                game = game.replace('-', '0'); // '-' means a miss, which scores 0

                FrameInfo[] frames = fillTheFrames(game);

                for (int i = 0; i < 10; i++) {

                    int total = 0;

                    // Fill in the bonus balls, except for the last frame which has already been done

                    for (int j = 0; j < frames.length; j++) {

                        if (frames[j].strike) {
                            frames[j].bonus1 = getNextBall(frames, j);
                            frames[j].bonus2 = getNextBall2(frames, j);
                            if(frames[j].bonus2 < 0) frames[j].bonus2 = 0;
                        } else if (frames[j].spare)
                            frames[j].bonus1 = getNextBall(frames, j);
                    }


                    System.out.printf("%02d  frame: %3s  strike: %5b  spare: %5b " + ""
                                      + "    base: %02d bonus1: %02d bonus2: %02d %n",
                                      i,
                                      frames[i].frameString,
                                      frames[i].strike,
                                      frames[i].spare,
                                      frames[i].baseScore,
                                      frames[i].bonus1,
                                      frames[i].bonus2
                    );


                    int www;
                    if (i == 9) {
                        int ba1 = frames[i].ball1;
                        int ba2 = frames[i].ball2;
                        int base = frames[i].baseScore;
                        int bo1 = frames[i].bonus1;
                        int bo2 = frames[i].bonus2;
                        www = 0;

                        for (int j = 0; j < 10; j++) {
                            total = total +
                                    frames[j].baseScore +
                                    frames[j].bonus1 +
                                    frames[j].bonus2;
                        }

                        System.out.println("score: " + total);
                    }

                    //System.out.println("-------------------------------------------");
                }
            }
        }
    }
