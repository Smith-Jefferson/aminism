<dal name="com.dal.test">
	<databaseSets>
		<databaseSet name="aminism" provider="mySqlProvider">
			<add name="aminism_w" databaseType="Master" sharding="" connectionString="aminism_w"/>
		</databaseSet>
	</databaseSets>
	<logListeners sampling="turn=off;low=60;high=5" encrypt="yes">
		<add name="clog" type="Arch.Data.Common.Logging.Listeners.CentralLoggingListener,Arch.Data" level="Information" setting=""/>
	</logListeners>
</dal>
