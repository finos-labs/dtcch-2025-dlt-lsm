package io.builders.demo.core

import java.security.SecureRandom

class RandomGenerator {

    static String transactionHash() {
        SecureRandom random = new SecureRandom()
        byte[] bytes = new byte[32]
        random.nextBytes(bytes)
        bytes.encodeHex().toString()
    }

    static String eventName() {
        SecureRandom random = new SecureRandom()

        String[] actions = [
            'Created', 'Updated', 'Deleted', 'Processed', 'Completed', 'Failed', 'Approved', 'Rejected', 'Scheduled', 'Initiated'
        ]
        String[] entities = [
            'Transaction', 'Order', 'Payment', 'Invoice', 'User', 'Session', 'Account', 'Subscription', 'Product', 'Shipment'
        ]

        String entity = entities[random.nextInt(entities.size())]
        String action = actions[random.nextInt(actions.size())]

        return "${entity}${action}"
    }

    static String address() {
        String[] hexChars = ('0'..'9') + ('a'..'f')
        SecureRandom random = new SecureRandom()

        String address = (1..40).collect { hexChars[random.nextInt(hexChars.size())] }.join()

        return "0x${address}"
    }

}
