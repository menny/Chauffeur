package net.evendanan.chauffeur.lib.permissions;

import net.evendanan.chauffeur.lib.FragmentChauffeur;

public interface PermissionsChauffeur extends FragmentChauffeur {
    void startPermissionsRequest(PermissionsRequest permissionsRequest);
}
