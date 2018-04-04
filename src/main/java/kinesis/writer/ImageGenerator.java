/*
 * Copyright 2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Amazon Software License (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 * http://aws.amazon.com/asl/
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package kinesis.writer;

import kinesis.utils.ImageUtils;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Generates random stock trades by picking randomly from a collection of stocks, assigning a
 * random price based on the mean, and picking a random quantity for the shares.
 *
 */

public class ImageGenerator {

    private static final List<BufferedImage> IMAGES = new ArrayList<BufferedImage>();
    static {
//        IMAGES.add(ImageUtils.getBufferedImage("data/images/frame-1.jpg"));
//        IMAGES.add(ImageUtils.getBufferedImage("data/images/frame-2.jpg"));
//        IMAGES.add(ImageUtils.getBufferedImage("data/images/frame-3.jpg"));
//        IMAGES.add(ImageUtils.getBufferedImage("data/images/frame-4.jpg"));
//        IMAGES.add(ImageUtils.getBufferedImage("data/images/frame-5.jpg"));
//        IMAGES.add(ImageUtils.getBufferedImage("data/images/frame-6.jpg"));
//        IMAGES.add(ImageUtils.getBufferedImage("data/images/frame-7.jpg"));
//        IMAGES.add(ImageUtils.getBufferedImage("data/images/frame-8.jpg"));
//        IMAGES.add(ImageUtils.getBufferedImage("data/images/frame-9.jpg"));
//        IMAGES.add(ImageUtils.getBufferedImage("data/images/frame-10.jpg"));
//        IMAGES.add(ImageUtils.getBufferedImage("data/images/frame-442.jpg"));
//        IMAGES.add(ImageUtils.getBufferedImage("data/images/frame-443.jpg"));
//        IMAGES.add(ImageUtils.getBufferedImage("data/images/frame-444.jpg"));
//        IMAGES.add(ImageUtils.getBufferedImage("data/images/frame-445.jpg"));
//        IMAGES.add(ImageUtils.getBufferedImage("data/images/frame-446.jpg"));

        IMAGES.add(ImageUtils.getBufferedImage("data/images/crying-boy-face.jpg"));
        IMAGES.add(ImageUtils.getBufferedImage("data/images/downlohappy-face1.jpg"));
        IMAGES.add(ImageUtils.getBufferedImage("data/images/putin.jpg"));
        IMAGES.add(ImageUtils.getBufferedImage("data/images/trump-and-kim.jpg"));
        IMAGES.add(ImageUtils.getBufferedImage("data/images/trump-and-putin.jpg"));
        IMAGES.add(ImageUtils.getBufferedImage("data/images/trump-smiling.jpg"));
    }

    private final Random random = new Random();

    /**
     * Return a random Image.
     *
     */
    public BufferedImage getRandomImage() {
        // pick a random stock
        return IMAGES.get(random.nextInt(IMAGES.size()));
    }
}
