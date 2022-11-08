import SwiftUI
import DirectDependency

struct ContentView: View {
    let greet = TransitiveDependencyTransitiveExpect().value

	var body: some View {
		Text(greet)
	}
}

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
		ContentView()
	}
}

func useMppApi(tc: TransitiveDependencyTransitiveExpect) {
    DirectDependency.UseTransitiveKt.consumeTransitiveExpectInCommon(tc: tc)
    
    DirectDependency.UseTransitive_iOSKt.consumeTransitiveExpectInIOS(tc: tc)
}
