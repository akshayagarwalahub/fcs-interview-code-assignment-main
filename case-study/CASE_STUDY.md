# Case Study Scenarios to discuss

## Scenario 1: Cost Allocation and Tracking
**Situation**: The company needs to track and allocate costs accurately across different Warehouses and Stores. The costs include labor, inventory, transportation, and overhead expenses.

**Task**: Discuss the challenges in accurately tracking and allocating costs in a fulfillment environment. Think about what are important considerations for this, what are previous experiences that you have you could related to this problem and elaborate some questions and considerations

Accurate cost allocation can be challenging because costs can be shared between multiple Warehouses, Stores and Products.
Some important considerations are:
- Define cost categories such as labor, inventory, transportation, rent and overhead.
- Decide whether costs should be tracked at Warehouse, Store, Product or shipment level.
- Define clear rules for allocating shared costs.
- Avoid double-counting the same cost.
- Track costs by day, month and financial year.
- Preserve historical costs when a Warehouse or Store changes.
- Handle delayed, corrected or missing cost data.

**Questions you may have and considerations:**

Questions I would clarify:
- What is the source of truth for each cost?
- What allocation rules does Finance currently use?
- Who owns and validates the cost data?
- How frequently is cost data updated?
- What reports are required by Finance and Operations?

## Scenario 2: Cost Optimization Strategies
**Situation**: The company wants to identify and implement cost optimization strategies for its fulfillment operations. The goal is to reduce overall costs without compromising service quality.

**Task**: Discuss potential cost optimization strategies for fulfillment operations and expected outcomes from that. How would you identify, prioritize and implement these strategies?

I would first identify the biggest cost drivers and then prioritize improvements based on expected savings and business impact.
Potential areas include:
- Improve Warehouse capacity utilization.
- Optimize transportation routes and shipments.
- Reduce unnecessary partial shipments.
- Improve inventory forecasting.
- Reduce excess or obsolete inventory.
- Optimize workforce based on demand.
- Review expensive or underutilized Warehouses.
I would measure the current cost first, define a target and then compare the result after implementation.
For example:

Current transportation cost = $ 100,000/month
Target reduction = 10%
Expected cost = $ 90,000/month

**Questions you may have and considerations:**

Questions I would clarify:
- Which costs are currently the highest?
- What level of service must be maintained?
- Which optimization has the highest potential saving?
- What is the implementation effort and business risk?
- How will savings be measured?

## Scenario 3: Integration with Financial Systems
**Situation**: The Cost Control Tool needs to integrate with existing financial systems to ensure accurate and timely cost data. The integration should support real-time data synchronization and reporting.

**Task**: Discuss the importance of integrating the Cost Control Tool with financial systems. What benefits the company would have from that and how would you ensure seamless integration and data synchronization?

Integration with financial systems is important because the Cost Control Tool should use reliable financial data and provide consistent reporting.
Benefits include:
- More accurate cost information.
- Reduced manual data entry.
- Faster reporting.
- Better visibility of actual vs planned costs.
- Easier reconciliation with Finance systems.
For seamless integration, I would consider:
- Clear API/data contracts.
- Real-time or scheduled synchronization based on business needs.
- Validation of incoming data.
- Error handling and retry mechanisms.
- Idempotency to avoid duplicate transactions.
- Audit logs for financial changes.

**Questions you may have and considerations:**
Questions I would clarify:
- Which financial system is the source of truth?
- Is real-time synchronization really required for every cost?
- What happens when the financial system is unavailable?
- How are corrections and duplicate records handled?
- What security and audit requirements exist?

## Scenario 4: Budgeting and Forecasting
**Situation**: The company needs to develop budgeting and forecasting capabilities for its fulfillment operations. The goal is to predict future costs and allocate resources effectively.

**Task**: Discuss the importance of budgeting and forecasting in fulfillment operations and what would you take into account designing a system to support accurate budgeting and forecasting?

Budgeting helps the company plan expected costs, while forecasting helps compare the expected costs with actual business conditions.
The system should consider:
- Historical costs.
- Expected sales and demand.
- Warehouse capacity and utilization.
- Labor costs.
- Transportation costs.
- Inventory levels.
- Seasonal variations.
- Planned Warehouse changes or replacements.
The system should allow comparison between:
Budget → Forecast → Actual

**Questions you may have and considerations:**
Questions I would clarify:
- How frequently should forecasts be updated?
- Which historical data should be used?
- Who owns the budget?
- How should unexpected costs be handled?
- Which KPIs should be used to measure forecast accuracy?

## Scenario 5: Cost Control in Warehouse Replacement
**Situation**: The company is planning to replace an existing Warehouse with a new one. The new Warehouse will reuse the Business Unit Code of the old Warehouse. The old Warehouse will be archived, but its cost history must be preserved.

**Task**: Discuss the cost control aspects of replacing a Warehouse. Why is it important to preserve cost history and how this relates to keeping the new Warehouse operation within budget?

When a Warehouse is replaced, the old Warehouse should be archived rather than deleted so that its historical costs remain available.
Important considerations are:
- Preserve the old Warehouse's cost history.
- Clearly distinguish the old and new Warehouse internally even if the Business Unit Code is reused.
- Record the replacement date.
- Track setup and migration costs separately.
- Compare the new Warehouse's actual costs with the approved budget.
- Monitor whether the new Warehouse is achieving the expected cost improvements.
For example, the Business Unit Code can remain the same for business purposes, while the system maintains separate Warehouse records/identifiers for historical tracking.

**Questions you may have and considerations:**

Questions I would clarify:
- What costs are associated with the replacement?
- What is the approved budget for the new Warehouse?
- How long should the old cost history be retained?
- How will old and new Warehouse costs be separated?
- Which KPIs determine whether the replacement was successful?

## Instructions for Candidates
Before starting the case study, read the [BRIEFING.md](BRIEFING.md) to quickly understand the domain, entities, business rules, and other relevant details.

**Analyze the Scenarios**: Carefully analyze each scenario and consider the tasks provided. To make informed decisions about the project's scope and ensure valuable outcomes, what key information would you seek to gather before defining the boundaries of the work? Your goal is to bridge technical aspects with business value, bringing a high level discussion; no need to deep dive.
