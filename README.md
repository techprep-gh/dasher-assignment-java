# Dasher assignment

The in-process core of the dasher assignment service. When an order is ready for
pickup, the service offers it to a random dasher who is online and free, and hands
the offer to one of the dispatch nodes, which take turns.

## Classes

All in `src/main/java/com/techprep/dasher/`:

- `AvailableDashers`: the dashers waiting for an offer. A list plus an id-to-position
  map, so adding, removing and reading by position are O(1).
- `RandomDasherPicker`: a uniform random pick from the pool.
- `RoundRobinRouter`: routes each order to the dispatch nodes in turn, skipping nodes
  that are not available. Nodes can be added while the service runs.
- `NodeAvailability`: each dispatch node's state, `"AVAILABLE"` or `"UNAVAILABLE"`.
  Health checks report a state string with `router.setAvailability(node, state)`;
  `markDown` and `markUp` are shorthands.
- `AssignmentService`: which dashers go online and offline through, and which assigns
  and completes orders.
- `errors/`: the service's exceptions, all subclasses of `AssignmentException`.

## Lifecycle

1. `goOnline(dasher)` puts a dasher in the pool.
2. `assign(order)` picks a dasher, routes the offer to a dispatch node and takes the
   dasher out of the pool while they deliver. It returns `Optional.empty()` when nobody
   is free.
3. `complete(orderId)` closes the delivery and puts the dasher back in the pool.
4. `goOffline(dasherId)` takes a free dasher out of the pool.

```java
AssignmentService service = new AssignmentService(new RoundRobinRouter(List.of("dispatch-1", "dispatch-2")));
service.goOnline(new Dasher("d1", "Ana"));
Assignment assignment = service.assign(new Order("o1", "Golden Wok", "12 Mission St")).orElseThrow();
service.complete(assignment.orderId());
```

## Tests

Needs JDK 17 or later and Maven.

```bash
mvn test
```
