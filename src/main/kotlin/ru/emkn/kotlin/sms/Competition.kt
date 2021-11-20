package ru.emkn.kotlin.sms

import java.io.File

class Competition (val name: String, val date: String, var orgs: List<Organisation> = listOf()){

    val participants: List<Participant>
        get() = this.orgs.flatMap { it.members }

    val size: Int
        get() = this.participants.size

    fun addOrganisationsToCompetition( pathApplications: String){
        val allOrgs = mutableListOf<Organisation>()
        for (file in File(pathApplications).listFiles()) {
            allOrgs.add(applicationToOrg(pathApplications+ "/"+file.name))
        }
        this.orgs = allOrgs
    }
    
    val sportClasses = getSportClasses()
}