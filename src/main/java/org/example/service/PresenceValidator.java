package org.example.service;

import org.example.model.Presence;

public interface PresenceValidator {
  boolean isValid(Presence presence);
}
