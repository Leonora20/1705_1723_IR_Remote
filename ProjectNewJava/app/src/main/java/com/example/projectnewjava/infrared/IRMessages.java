package com.example.projectnewjava.infrared;

public class IRMessages {

    //Speaker
    public static IRMessage SPEAKER_ON;

    //TV
    public static IRMessage TV_ON;
    public static IRMessage TV_VOLUME_UP;
    public static IRMessage TV_VOLUME_DOWN;

    public static void initialize() {

        //Speaker
        SPEAKER_ON = IRSpeakerFactory.create(IRSpeakerFactory.TYPE_12_BITS, 84, 1, 3);

        //TV
        TV_ON = IRTVFactory.create(16, 32, 2);
        TV_VOLUME_UP = IRTVFactory.create(64, 32, 2);
        TV_VOLUME_DOWN = IRTVFactory.create(192, 32, 2);
    }
}
