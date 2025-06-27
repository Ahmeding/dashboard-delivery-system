package com.hitecart.deliverysystem.model.enums;

public enum ContractStatus {
    CREATED,
    ACTIVE,
    SUSPENDED, // The contract is temporarily paused (e.g., due to illness, parental leave, or investigation). 2 - Rights/obligations may be frozen but not terminated.
    TERMINATED /*(Ended Early)
    The contract ended before its planned end date.

    Reasons:

    Resignation (employee quits).

    Dismissal (employer fires employee).

    Mutual Agreement (both parties agree to end early).

    Breach (one party violates terms).*/

    ,EXPIRED /*/ Finished
    The contract ended naturally at its agreed end date (common for fixed-term contracts).

    No renewal or extension occurs.

            Example: "The 1-year contract expired on December 31, 2025."*/

    ,RENEWED /*/ Extended
    The contract continues beyond its original end date (new terms may apply).

    Example: "The contract was renewed for another year."
*/
    ,COMPLETED
   /* Used for task-based contracts (ends when a specific project/result is delivered).

    Example: "The contract completed after the software launch."*/


}
