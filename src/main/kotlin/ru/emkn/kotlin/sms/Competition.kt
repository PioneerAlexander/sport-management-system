package ru.emkn.kotlin.sms

class Competition (val name: String, val date: String, val orgs: List<Organisation>){
    val participants: List<Participant>
        get() = this.orgs.flatMap { it.members }

    val size: Int
        get() = this.participants.size
}