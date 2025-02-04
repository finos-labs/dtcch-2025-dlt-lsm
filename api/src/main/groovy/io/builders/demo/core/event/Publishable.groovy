package io.builders.demo.core.event

import groovy.transform.PackageScope

trait Publishable {

    // When not null, the event has been retried and should not be retried again.
    @PackageScope
    UUID eventIdentifier

}
